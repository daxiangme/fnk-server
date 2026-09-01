package com.fnk.app.infra.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.infra.api.constants.InfraPermissionConstants;
import com.fnk.app.infra.api.facade.CodeGenFacade;
import com.fnk.app.infra.api.model.query.CodeGenTableQuery;
import com.fnk.app.infra.api.model.request.CodeGenFieldBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenRelationBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenTableImportAO;
import com.fnk.app.infra.api.model.request.CodeGenTableUpdateAO;
import com.fnk.app.infra.api.model.response.*;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import com.fnk.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/infra/codegen")
@Tag(name = "代码生成", description = "企业级代码生成相关接口")
@AllArgsConstructor
public class CodeGenController extends BaseController {
    private final CodeGenFacade codeGenFacade;

    @GetMapping("/database/tables")
    @Operation(summary = "数据库表列表")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_VIEW)
    public RestResponse<List<DatabaseTableVO>> databaseTables(
            @RequestParam(required = false) String tableName,
            @RequestParam(defaultValue = "false") boolean excludeImported) {
        return RestResponse.ok(codeGenFacade.databaseTables(tableName, excludeImported));
    }

    @GetMapping("/tables")
    @Operation(summary = "代码生成表配置列表")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_VIEW)
    public RestResponse<PageVO<CodeGenTableVO>> tables(CodeGenTableQuery query) {
        return RestResponse.ok(codeGenFacade.page(query));
    }

    @PostMapping("/tables/import")
    @Operation(summary = "导入数据表")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_IMPORT)
    public RestResponse<List<CodeGenTableVO>> importTables(@RequestBody @Validated CodeGenTableImportAO req) {
        return RestResponse.ok(codeGenFacade.importTables(req));
    }

    @GetMapping("/tables/{id}")
    @Operation(summary = "代码生成表配置详情")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_VIEW)
    public RestResponse<CodeGenTableVO> detail(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.detail(id));
    }

    @PutMapping("/tables/{id}")
    @Operation(summary = "更新代码生成表配置")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_UPDATE)
    public RestResponse<CodeGenTableVO> update(@PathVariable String id, @RequestBody @Validated CodeGenTableUpdateAO req) {
        return RestResponse.ok(codeGenFacade.updateConfig(id, req));
    }

    @PostMapping("/tables/{id}/sync-fields")
    @Operation(summary = "同步字段结构")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_UPDATE)
    public RestResponse<List<CodeGenFieldVO>> syncFields(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.syncFields(id));
    }

    @GetMapping("/tables/{id}/fields")
    @Operation(summary = "字段映射配置")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_VIEW)
    public RestResponse<List<CodeGenFieldVO>> fields(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.fields(id));
    }

    @PutMapping("/tables/{id}/fields")
    @Operation(summary = "批量更新字段映射配置")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_UPDATE)
    public RestResponse<List<CodeGenFieldVO>> updateFields(@PathVariable String id, @RequestBody @Validated CodeGenFieldBatchUpdateAO req) {
        return RestResponse.ok(codeGenFacade.updateFields(id, req));
    }

    @GetMapping("/tables/{id}/relations")
    @Operation(summary = "表关系分析结果")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_VIEW)
    public RestResponse<List<CodeGenRelationVO>> relations(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.relations(id));
    }

    @PostMapping("/tables/{id}/relations/analyze")
    @Operation(summary = "分析表关系")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_UPDATE)
    public RestResponse<List<CodeGenRelationVO>> analyzeRelations(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.analyzeRelations(id));
    }

    @PutMapping("/tables/{id}/relations")
    @Operation(summary = "批量更新表关系配置")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_UPDATE)
    public RestResponse<List<CodeGenRelationVO>> updateRelations(@PathVariable String id, @RequestBody CodeGenRelationBatchUpdateAO req) {
        return RestResponse.ok(codeGenFacade.updateRelations(id, req));
    }

    @PostMapping("/tables/{id}/preview")
    @Operation(summary = "代码生成预览")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_PREVIEW)
    public RestResponse<CodeGenPreviewVO> preview(@PathVariable String id) {
        return RestResponse.ok(codeGenFacade.preview(id));
    }

    @PostMapping("/tables/{id}/download")
    @Operation(summary = "下载生成代码")
    @SaCheckPermission(InfraPermissionConstants.CODEGEN_DOWNLOAD)
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        byte[] bytes = codeGenFacade.download(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("codegen-" + id + ".zip")
                .build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}
