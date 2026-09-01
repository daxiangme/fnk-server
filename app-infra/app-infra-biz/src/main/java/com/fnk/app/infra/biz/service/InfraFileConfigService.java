package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fnk.app.infra.api.model.query.InfraFileConfigQuery;
import com.fnk.app.infra.biz.dal.entity.InfraFileConfigDO;
import com.fnk.app.infra.biz.dal.mapper.InfraFileConfigMapper;
import com.fnk.common.bean.exception.LogicException;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件存储配置服务。
 *
 * @author Enigma
 */
@Service
public class InfraFileConfigService extends BaseService<InfraFileConfigMapper, InfraFileConfigDO> {
    public static final String STORAGE_LOCAL = "local";
    public static final String STORAGE_S3 = "s3";
    private static final long DEFAULT_MAX_SIZE_MB = 50L;

    public PageVO<InfraFileConfigDO> page(InfraFileConfigQuery query) {
        return this.basicPage(query, InfraFileConfigDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getName()), InfraFileConfigDO::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getStorageType()), InfraFileConfigDO::getStorageType, query.getStorageType())
                .eq(query.getMaster() != null, InfraFileConfigDO::getMaster, query.getMaster()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfraFileConfigDO create(InfraFileConfigDO req) {
        sanitizeAndValidate(req, null);
        if (this.count() == 0) {
            req.setMaster(true);
        }
        if (Boolean.TRUE.equals(req.getMaster())) {
            assertCanBeMaster(req);
            clearMaster();
        }
        return super.create(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfraFileConfigDO update(String id, InfraFileConfigDO req) {
        InfraFileConfigDO exists = this.detail(id);
        sanitizeAndValidate(req, id);
        req.setMaster(exists.getMaster());
        return super.update(id, req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSingle(String id) {
        InfraFileConfigDO config = this.detail(id);
        AssertUtils.isTrue(Boolean.TRUE.equals(config.getMaster()), "主文件配置不能删除");
        super.removeSingle(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public InfraFileConfigDO setMaster(String id) {
        InfraFileConfigDO config = this.detail(id);
        assertCanBeMaster(config);
        clearMaster();
        config.setMaster(true);
        AssertUtils.isFalse(config.updateById(), "设置主文件配置失败");
        return config;
    }

    public InfraFileConfigDO masterConfig() {
        InfraFileConfigDO config = this.getFirst(new LambdaQueryWrapper<InfraFileConfigDO>()
                .eq(InfraFileConfigDO::getMaster, true));
        AssertUtils.isNull(config, "请先启用一个文件配置");
        return config;
    }

    public String testConfig(String id) {
        InfraFileConfigDO config = this.detail(id);
        sanitizeForSave(config);
        if (STORAGE_LOCAL.equals(config.getStorageType())) {
            Path root = storageRoot(config);
            Path probe = root.resolve(".file-config-test").normalize();
            AssertUtils.isFalse(probe.startsWith(root), "文件测试路径非法");
            try {
                Files.createDirectories(root);
                Files.writeString(probe, "ok", StandardCharsets.UTF_8);
                Files.deleteIfExists(probe);
                return "本地文件配置测试通过";
            } catch (IOException e) {
                throw new IllegalStateException("本地文件配置测试失败", e);
            }
        }
        return "S3 配置校验通过，客户端连通性测试暂未接入";
    }

    Path resolveLocalPath(InfraFileConfigDO config, String storagePath) {
        Path root = storageRoot(config);
        Path target = root.resolve(storagePath).normalize();
        AssertUtils.isFalse(target.startsWith(root), "文件存储路径非法");
        return target;
    }

    String buildFileUrl(InfraFileConfigDO config, String fileId) {
        String path = "/infra/files/" + fileId + "/content";
        if (StrUtil.isBlank(config.getDomain())) {
            return path;
        }
        return StrUtil.removeSuffix(config.getDomain(), "/") + path;
    }

    void sanitizeForSave(InfraFileConfigDO config) {
        AssertUtils.isNull(config, "文件配置不能为空");
        config.setName(StrUtil.trim(config.getName()));
        config.setStorageType(StrUtil.blankToDefault(StrUtil.trim(config.getStorageType()), STORAGE_LOCAL));
        config.setBasePath(StrUtil.trim(config.getBasePath()));
        config.setDomain(StrUtil.trim(config.getDomain()));
        config.setEndpoint(StrUtil.trim(config.getEndpoint()));
        config.setBucket(StrUtil.trim(config.getBucket()));
        config.setAccessKey(StrUtil.trim(config.getAccessKey()));
        config.setAccessSecret(StrUtil.trim(config.getAccessSecret()));
        config.setRemark(StrUtil.trim(config.getRemark()));
        if (config.getMaxSizeMb() == null) {
            config.setMaxSizeMb(DEFAULT_MAX_SIZE_MB);
        }
        if (config.getMaster() == null) {
            config.setMaster(false);
        }
        if (config.getEnablePathStyleAccess() == null) {
            config.setEnablePathStyleAccess(false);
        }

        AssertUtils.isBlank(config.getName(), "配置名称不能为空");
        AssertUtils.isTrue(config.getMaxSizeMb() <= 0, "最大上传大小必须大于 0");
        if (STORAGE_LOCAL.equals(config.getStorageType())) {
            AssertUtils.isBlank(config.getBasePath(), "本地存储路径不能为空");
            return;
        }
        if (STORAGE_S3.equals(config.getStorageType())) {
            AssertUtils.isBlank(config.getEndpoint(), "Endpoint 不能为空");
            AssertUtils.isBlank(config.getBucket(), "Bucket 不能为空");
            AssertUtils.isBlank(config.getAccessKey(), "Access Key 不能为空");
            AssertUtils.isBlank(config.getAccessSecret(), "Access Secret 不能为空");
            return;
        }
        throw new LogicException("不支持的文件存储类型：" + config.getStorageType());
    }

    private void sanitizeAndValidate(InfraFileConfigDO config, String id) {
        sanitizeForSave(config);
        LambdaQueryWrapper<InfraFileConfigDO> wrapper = new LambdaQueryWrapper<InfraFileConfigDO>()
                .eq(InfraFileConfigDO::getName, config.getName());
        if (StrUtil.isNotBlank(id)) {
            wrapper.ne(InfraFileConfigDO::getId, id);
        }
        AssertUtils.isTrue(this.count(wrapper) > 0, "文件配置名称已存在");
    }

    private Path storageRoot(InfraFileConfigDO config) {
        String basePath = StrUtil.blankToDefault(config.getBasePath(),
                Paths.get(System.getProperty("user.dir"), "dev", "uploads").toString());
        Path path = Paths.get(basePath);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }
        return path.normalize();
    }

    private void clearMaster() {
        this.update(new LambdaUpdateWrapper<InfraFileConfigDO>()
                .set(InfraFileConfigDO::getMaster, false)
                .eq(InfraFileConfigDO::getMaster, true));
    }

    private void assertCanBeMaster(InfraFileConfigDO config) {
        AssertUtils.isFalse(STORAGE_LOCAL.equals(config.getStorageType()),
                "当前仅支持本地文件配置设为主配置");
    }

    @Override
    public String getServiceModelName() {
        return "文件配置";
    }
}
