package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.facade.RoleFacade;
import com.fnk.app.system.api.model.query.RolePageQuery;
import com.fnk.app.system.api.model.request.RoleCreateAO;
import com.fnk.app.system.api.model.request.RoleUpdateAO;
import com.fnk.app.system.api.model.response.RoleInfoVO;
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
 * 角色信息控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "角色信息", description = "角色信息相关接口")
@AllArgsConstructor
public class RoleInfoController extends BaseController {
    private final RoleFacade roleFacade;

    @GetMapping
    @Operation(summary = "角色信息列表")
    @SaCheckPermission("system:role:view")
    public RestResponse<PageVO<RoleInfoVO>> list(RolePageQuery query) {
        return RestResponse.ok(roleFacade.page(query));
    }

    @GetMapping("/all")
    @Operation(summary = "角色信息列表")
    @SaCheckPermission("system:role:view")
    public RestResponse<List<RoleInfoVO>> listAll() {
        return RestResponse.ok(roleFacade.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定ID角色信息的详情")
    @SaCheckPermission("system:role:view")
    public RestResponse<RoleInfoVO> detail(@PathVariable String id) {
        return RestResponse.ok(roleFacade.detail(id));
    }

    @GetMapping("/{id}/menus")
    @Operation(summary = "获取指定角色ID的菜单ID列表")
    @SaCheckPermission("system:role:permission")
    public RestResponse<List<String>> queryRoleMenuIds(@PathVariable String id) {
        return RestResponse.ok(roleFacade.queryRoleMenuIds(id));
    }

    @PostMapping
    @Operation(summary = "创建角色信息")
    @SaCheckPermission("system:role:create")
    public RestResponse<RoleInfoVO> create(@RequestBody @Validated RoleCreateAO req) {
        return RestResponse.ok(roleFacade.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新指定ID的角色信息")
    @SaCheckPermission("system:role:update")
    public RestResponse<RoleInfoVO> update(@PathVariable String id, @RequestBody @Validated RoleUpdateAO req) {
        return RestResponse.ok(roleFacade.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除指定ID的角色信息")
    @SaCheckPermission("system:role:delete")
    public RestResponse<Integer> remove(@PathVariable String id) {
        return RestResponse.ok(roleFacade.remove(id));
    }

    @DeleteMapping
    @Operation(summary = "批量删除指定ID的角色信息")
    @SaCheckPermission("system:role:delete")
    public RestResponse<Void> remove(@RequestParam("id-list") List<String> idList) {
        roleFacade.remove(idList);
        return RestResponse.ok();
    }
}
