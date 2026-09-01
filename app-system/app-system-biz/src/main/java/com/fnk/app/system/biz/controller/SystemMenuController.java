package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.facade.MenuFacade;
import com.fnk.app.system.api.model.query.MenuQuery;
import com.fnk.app.system.api.model.request.MenuCreateAO;
import com.fnk.app.system.api.model.request.MenuUpdateAO;
import com.fnk.app.system.api.model.response.SystemMenuVO;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/menu")
@Tag(name = "系统菜单", description = "系统菜单相关接口")
@Slf4j
@AllArgsConstructor
public class SystemMenuController extends BaseController {
    private final MenuFacade menuFacade;

    @GetMapping
    @Operation(summary = "系统菜单列表")
    @SaCheckPermission("system:menu:view")
    public RestResponse<List<SystemMenuVO>> list(MenuQuery query) {
        log.info("params: {}", query);
        return RestResponse.ok(menuFacade.list(query));
    }

    @GetMapping("/root/{rootId}")
    @Operation(summary = "根据rootID查询菜单")
    @SaCheckPermission("system:menu:view")
    public RestResponse<List<SystemMenuVO>> listByRootId(@PathVariable String rootId) {
        return RestResponse.ok(menuFacade.listByRootId(rootId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定ID系统菜单的详情")
    @SaCheckPermission("system:menu:view")
    public RestResponse<SystemMenuVO> detail(@PathVariable String id) {
        return RestResponse.ok(menuFacade.detail(id));
    }

    @PostMapping
    @Operation(summary = "创建系统菜单")
    @SaCheckPermission("system:menu:create")
    public RestResponse<SystemMenuVO> create(@RequestBody @Validated MenuCreateAO req) {
        return RestResponse.ok(menuFacade.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新指定ID的系统菜单")
    @SaCheckPermission("system:menu:update")
    public RestResponse<SystemMenuVO> update(@PathVariable String id, @RequestBody @Validated MenuUpdateAO req) {
        return RestResponse.ok(menuFacade.update(id, req));
    }

    @PostMapping("/permission-cache/refresh")
    @Operation(summary = "刷新角色权限缓存")
    @SaCheckPermission("system:menu:update")
    public RestResponse<Void> refreshPermissionCache() {
        menuFacade.refreshPermissionCache();
        return RestResponse.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除指定ID的系统菜单")
    @SaCheckPermission("system:menu:delete")
    public RestResponse<Void> remove(@PathVariable String id) {
        menuFacade.remove(id);
        return RestResponse.ok();
    }

    @DeleteMapping
    @Operation(summary = "批量删除指定ID的系统菜单")
    @SaCheckPermission("system:menu:delete")
    public RestResponse<Void> remove(@RequestParam("id-list") List<String> idList) {
        menuFacade.remove(idList);
        return RestResponse.ok();
    }
}
