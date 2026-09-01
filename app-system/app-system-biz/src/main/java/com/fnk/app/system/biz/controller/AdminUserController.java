package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.facade.AdminUserFacade;
import com.fnk.app.system.api.model.query.AdminUserPageQuery;
import com.fnk.app.system.api.model.request.AdminUserCreateAO;
import com.fnk.app.system.api.model.request.AdminUserUpdateAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import com.fnk.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/admin/user")
@Tag(name = "系统用户", description = "系统用户相关接口")
@AllArgsConstructor
public class AdminUserController extends BaseController {
    private final AdminUserFacade adminUserFacade;

    @GetMapping
    @Operation(summary = "系统用户列表")
    @SaCheckPermission("system:user:view")
    public RestResponse<PageVO<AdminUserVO>> list(AdminUserPageQuery query) {
        return RestResponse.ok(adminUserFacade.page(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定ID系统用户的详情")
    @SaCheckPermission("system:user:view")
    public RestResponse<AdminUserVO> detail(@PathVariable String id) {
        return RestResponse.ok(adminUserFacade.detail(id));
    }

    @GetMapping("/{id}/roles")
    @Operation(summary = "获取指定ID系统用户的角色ID列表")
    @SaCheckPermission("system:user:view")
    public RestResponse<List<String>> queryUserRoleIds(@PathVariable String id) {
        return RestResponse.ok(adminUserFacade.queryUserRoleIds(id));
    }

    @PostMapping
    @Operation(summary = "创建系统用户")
    @SaCheckPermission("system:user:create")
    public RestResponse<AdminUserVO> create(@RequestBody @Validated AdminUserCreateAO req) {
        return RestResponse.ok(adminUserFacade.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新指定ID的系统用户")
    @SaCheckPermission("system:user:update")
    public RestResponse<AdminUserVO> update(@PathVariable String id, @RequestBody @Validated AdminUserUpdateAO req) {
        return RestResponse.ok(adminUserFacade.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除指定ID的系统用户")
    @SaCheckPermission("system:user:delete")
    public RestResponse<Void> remove(@PathVariable String id) {
        adminUserFacade.remove(id);
        return RestResponse.ok();
    }

    @DeleteMapping
    @Operation(summary = "批量删除指定ID的系统用户")
    @SaCheckPermission("system:user:delete")
    public RestResponse<Void> remove(@RequestParam("id-list") List<String> idList) {
        adminUserFacade.remove(idList);
        return RestResponse.ok();
    }
}
