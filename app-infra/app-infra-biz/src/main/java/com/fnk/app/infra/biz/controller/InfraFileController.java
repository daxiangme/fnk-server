package com.fnk.app.infra.biz.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.infra.api.constants.InfraPermissionConstants;
import com.fnk.app.infra.api.model.query.InfraFileQuery;
import com.fnk.app.infra.api.model.response.InfraFileVO;
import com.fnk.app.infra.biz.convert.InfraConvert;
import com.fnk.app.infra.biz.dal.entity.InfraFileDO;
import com.fnk.app.infra.biz.service.InfraFileService;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件资源控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/infra/files")
@Tag(name = "文件管理", description = "文件资源相关接口")
@AllArgsConstructor
public class InfraFileController {
    private final InfraFileService fileService;

    @GetMapping
    @Operation(summary = "文件资源列表")
    @SaCheckPermission(InfraPermissionConstants.FILE_VIEW)
    public RestResponse<PageVO<InfraFileVO>> page(InfraFileQuery query) {
        return RestResponse.ok(InfraConvert.toInfraFilePage(fileService.page(query)));
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @SaCheckPermission(InfraPermissionConstants.FILE_UPLOAD)
    public RestResponse<InfraFileVO> upload(@RequestParam("file") MultipartFile file) {
        return RestResponse.ok(InfraConvert.toInfraFileVO(fileService.upload(file)));
    }

    @GetMapping("/{id}/content")
    @Operation(summary = "读取文件内容")
    @SaCheckLogin
    public ResponseEntity<Resource> content(@PathVariable String id) {
        InfraFileDO file = fileService.detail(id);
        Resource resource = fileService.content(id);
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        ContentDisposition disposition = ContentDisposition.inline()
                .filename(file.getOriginalName())
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件")
    @SaCheckPermission(InfraPermissionConstants.FILE_DELETE)
    public RestResponse<Void> delete(@PathVariable String id) {
        fileService.removeSingle(id);
        return RestResponse.ok();
    }
}
