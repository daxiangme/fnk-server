package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fnk.app.infra.api.model.query.InfraFileQuery;
import com.fnk.app.infra.biz.dal.entity.InfraFileConfigDO;
import com.fnk.app.infra.biz.dal.entity.InfraFileDO;
import com.fnk.app.infra.biz.dal.mapper.InfraFileMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件资源服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class InfraFileService extends BaseService<InfraFileMapper, InfraFileDO> {
    private static final DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final InfraFileConfigService fileConfigService;

    public PageVO<InfraFileDO> page(InfraFileQuery query) {
        return this.basicPage(query, InfraFileDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getOriginalName()), InfraFileDO::getOriginalName, query.getOriginalName())
                .like(StrUtil.isNotBlank(query.getFileName()), InfraFileDO::getFileName, query.getFileName())
                .like(StrUtil.isNotBlank(query.getContentType()), InfraFileDO::getContentType, query.getContentType())
                .eq(StrUtil.isNotBlank(query.getStorageType()), InfraFileDO::getStorageType, query.getStorageType())
                .eq(StrUtil.isNotBlank(query.getConfigId()), InfraFileDO::getConfigId, query.getConfigId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public InfraFileDO upload(MultipartFile file) {
        AssertUtils.isNull(file, "上传文件不能为空");
        AssertUtils.isTrue(file.isEmpty(), "上传文件不能为空");
        InfraFileConfigDO config = fileConfigService.masterConfig();
        AssertUtils.isFalse(InfraFileConfigService.STORAGE_LOCAL.equals(config.getStorageType()),
                "当前仅支持本地文件配置上传");
        long maxSizeMb = config.getMaxSizeMb();
        AssertUtils.isTrue(file.getSize() > maxSizeMb * 1024 * 1024,
                "上传文件大小不能超过 " + maxSizeMb + "MB");

        String originalName = StrUtil.blankToDefault(file.getOriginalFilename(), "unknown");
        String fileName = IdUtil.fastSimpleUUID() + extension(originalName);
        String storagePath = DATE_PATH_FORMATTER.format(LocalDate.now()) + "/" + fileName;
        Path targetPath = fileConfigService.resolveLocalPath(config, storagePath);

        try {
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath);
        } catch (IOException e) {
            throw new IllegalStateException("保存上传文件失败", e);
        }

        InfraFileDO entity = new InfraFileDO();
        entity.setOriginalName(originalName);
        entity.setFileName(fileName);
        entity.setContentType(StrUtil.blankToDefault(file.getContentType(), "application/octet-stream"));
        entity.setFileSize(file.getSize());
        entity.setStorageType(config.getStorageType());
        entity.setConfigId(config.getId());
        entity.setConfigName(config.getName());
        entity.setStoragePath(storagePath);
        super.create(entity);
        entity.setUrl(fileConfigService.buildFileUrl(config, entity.getId()));
        AssertUtils.isFalse(entity.updateById(), "更新文件访问地址失败");
        return entity;
    }

    public Resource content(String id) {
        InfraFileDO file = this.detail(id);
        try {
            Path path = localPath(file);
            Resource resource = new UrlResource(path.toUri());
            AssertUtils.isFalse(resource.exists() && resource.isReadable(), "文件不存在或不可读");
            return resource;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("文件路径非法", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSingle(String id) {
        InfraFileDO file = this.detail(id);
        super.removeSingle(id);
        try {
            Files.deleteIfExists(localPath(file));
        } catch (IOException e) {
            throw new IllegalStateException("删除本地文件失败", e);
        }
    }

    private Path localPath(InfraFileDO file) {
        Path oldPath = Paths.get(file.getStoragePath()).normalize();
        if (StrUtil.isBlank(file.getConfigId()) || oldPath.isAbsolute()) {
            return oldPath;
        }
        InfraFileConfigDO config = fileConfigService.detail(file.getConfigId());
        AssertUtils.isFalse(InfraFileConfigService.STORAGE_LOCAL.equals(config.getStorageType()),
                "当前仅支持本地文件读取");
        return fileConfigService.resolveLocalPath(config, file.getStoragePath());
    }

    private String extension(String originalName) {
        int index = originalName.lastIndexOf('.');
        if (index < 0 || index == originalName.length() - 1) {
            return "";
        }
        return originalName.substring(index);
    }

    @Override
    public String getServiceModelName() {
        return "文件资源";
    }
}
