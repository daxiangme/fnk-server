package com.fnk.app.infra.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.infra.api.constants.InfraPermissionConstants;
import com.fnk.app.infra.api.model.query.InfraFileConfigQuery;
import com.fnk.app.infra.api.model.request.InfraFileConfigAO;
import com.fnk.app.infra.api.model.response.InfraFileConfigVO;
import com.fnk.app.infra.biz.convert.InfraConvert;
import com.fnk.app.infra.biz.dal.entity.InfraFileConfigDO;
import com.fnk.app.infra.biz.service.InfraFileConfigService;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文件配置控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/infra/file-configs")
@Tag(name = "文件配置", description = "文件存储配置相关接口")
@AllArgsConstructor
public class InfraFileConfigController {
    private final InfraFileConfigService fileConfigService;

    @GetMapping
    @Operation(summary = "文件配置列表")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_VIEW)
    public RestResponse<PageVO<InfraFileConfigVO>> page(InfraFileConfigQuery query) {
        return RestResponse.ok(InfraConvert.toInfraFileConfigPage(fileConfigService.page(query)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "文件配置详情")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_VIEW)
    public RestResponse<InfraFileConfigVO> detail(@PathVariable String id) {
        return RestResponse.ok(InfraConvert.toInfraFileConfigVO(fileConfigService.detail(id)));
    }

    @PostMapping
    @Operation(summary = "新增文件配置")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_CREATE)
    public RestResponse<InfraFileConfigVO> create(@RequestBody @Validated InfraFileConfigAO req) {
        return RestResponse.ok(InfraConvert.toInfraFileConfigVO(fileConfigService.create(toDO(req))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑文件配置")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_UPDATE)
    public RestResponse<InfraFileConfigVO> update(@PathVariable String id, @RequestBody @Validated InfraFileConfigAO req) {
        return RestResponse.ok(InfraConvert.toInfraFileConfigVO(fileConfigService.update(id, toDO(req))));
    }

    @PutMapping("/{id}/master")
    @Operation(summary = "设置主文件配置")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_MASTER)
    public RestResponse<InfraFileConfigVO> setMaster(@PathVariable String id) {
        return RestResponse.ok(InfraConvert.toInfraFileConfigVO(fileConfigService.setMaster(id)));
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "测试文件配置")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_TEST)
    public RestResponse<String> test(@PathVariable String id) {
        return RestResponse.ok(fileConfigService.testConfig(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件配置")
    @SaCheckPermission(InfraPermissionConstants.FILE_CONFIG_DELETE)
    public RestResponse<Void> delete(@PathVariable String id) {
        fileConfigService.removeSingle(id);
        return RestResponse.ok();
    }

    private InfraFileConfigDO toDO(InfraFileConfigAO source) {
        InfraFileConfigDO target = new InfraFileConfigDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
