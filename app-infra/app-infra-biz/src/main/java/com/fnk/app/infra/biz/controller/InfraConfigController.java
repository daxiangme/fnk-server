package com.fnk.app.infra.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.infra.api.constants.InfraPermissionConstants;
import com.fnk.app.infra.api.model.query.InfraConfigQuery;
import com.fnk.app.infra.api.model.request.InfraConfigAO;
import com.fnk.app.infra.api.model.response.InfraConfigVO;
import com.fnk.app.infra.biz.convert.InfraConvert;
import com.fnk.app.infra.biz.dal.entity.InfraConfigDO;
import com.fnk.app.infra.biz.service.InfraConfigService;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统参数配置控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/infra/config")
@Tag(name = "系统参数配置", description = "系统参数配置相关接口")
@AllArgsConstructor
public class InfraConfigController {
    private final InfraConfigService configService;

    @GetMapping
    @Operation(summary = "系统参数配置列表")
    @SaCheckPermission(InfraPermissionConstants.CONFIG_VIEW)
    public RestResponse<PageVO<InfraConfigVO>> page(InfraConfigQuery query) {
        return RestResponse.ok(InfraConvert.toInfraConfigPage(configService.page(query)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "系统参数配置详情")
    @SaCheckPermission(InfraPermissionConstants.CONFIG_VIEW)
    public RestResponse<InfraConfigVO> detail(@PathVariable String id) {
        return RestResponse.ok(InfraConvert.toInfraConfigVO(configService.detail(id)));
    }

    @PostMapping
    @Operation(summary = "创建系统参数配置")
    @SaCheckPermission(InfraPermissionConstants.CONFIG_CREATE)
    public RestResponse<InfraConfigVO> create(@RequestBody @Validated InfraConfigAO req) {
        return RestResponse.ok(InfraConvert.toInfraConfigVO(configService.create(toDO(req))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新系统参数配置")
    @SaCheckPermission(InfraPermissionConstants.CONFIG_UPDATE)
    public RestResponse<InfraConfigVO> update(@PathVariable String id, @RequestBody @Validated InfraConfigAO req) {
        return RestResponse.ok(InfraConvert.toInfraConfigVO(configService.update(id, toDO(req))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除系统参数配置")
    @SaCheckPermission(InfraPermissionConstants.CONFIG_DELETE)
    public RestResponse<Void> delete(@PathVariable String id) {
        configService.removeSingle(id);
        return RestResponse.ok();
    }

    private InfraConfigDO toDO(InfraConfigAO source) {
        InfraConfigDO target = new InfraConfigDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
