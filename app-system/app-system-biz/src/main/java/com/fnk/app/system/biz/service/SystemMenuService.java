package com.fnk.app.system.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.enums.MenuTypeEnum;
import com.fnk.common.db.impl.BaseService;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.mapper.SystemMenuMapper;
import com.fnk.common.tools.lang.AssertUtils;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
* 系统菜单 服务实现层
*
* @author Enigma
* @since 2023-12-18
*/
@Service
@AllArgsConstructor
public class SystemMenuService extends BaseService<SystemMenuMapper, SystemMenuDO> {

    private RoleMenuService roleMenuService;

    @Override
    public SystemMenuDO create(SystemMenuDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemMenuDO update(String id, SystemMenuDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    public List<SystemMenuDO> listByRootId(String rootId) {
        return this.list(new LambdaQueryWrapper<SystemMenuDO>().eq(SystemMenuDO::getRootId, rootId));
    }

    public List<SystemMenuDO> listAllAuthorizedMenus() {
        return this.list(new LambdaQueryWrapper<SystemMenuDO>()
                .ne(SystemMenuDO::getType, MenuTypeEnum.BUTTON)
                .orderByAsc(SystemMenuDO::getOrderSort));
    }

    public List<String> listAllPermissionKeys() {
        return this.list(new LambdaQueryWrapper<SystemMenuDO>()
                        .select(SystemMenuDO::getPermission)
                        .isNotNull(SystemMenuDO::getPermission))
                .stream()
                .map(SystemMenuDO::getPermission)
                .filter(permission -> !permission.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteMenu(String menuId) {
        return deleteMenus(List.of(menuId));
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteMenus(Collection<String> menuIds) {
        List<String> allMenuIds = collectMenuIds(menuIds);
        if (allMenuIds.isEmpty()) {
            return 0;
        }
        int count = roleMenuService.deleteByMenuIds(allMenuIds);
        AssertUtils.isFalse(this.removeBatchByIds(allMenuIds), "删除菜单失败");
        count += allMenuIds.size();
        return count;
    }

    private List<String> collectMenuIds(Collection<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return List.of();
        }
        Set<String> result = new LinkedHashSet<>();
        ArrayDeque<String> queue = new ArrayDeque<>(menuIds);

        while (!queue.isEmpty()) {
            String menuId = queue.removeFirst();
            if (menuId == null || menuId.isBlank() || !result.add(menuId)) {
                continue;
            }
            List<String> childIds = this.list(new LambdaQueryWrapper<SystemMenuDO>()
                    .eq(SystemMenuDO::getRootId, menuId))
                    .stream()
                    .map(SystemMenuDO::getId)
                    .toList();
            queue.addAll(childIds);
        }

        return new ArrayList<>(result);
    }

    private void normalizeAndValidate(SystemMenuDO menu) {
        AssertUtils.isNull(menu, "菜单信息不能为空");
        AssertUtils.isBlank(menu.getName(), "菜单名称不能为空");
        AssertUtils.isBlank(menu.getRouteKey(), "路由 Key 不能为空");
        AssertUtils.isNull(menu.getType(), "菜单类型不能为空");
        if (StrUtil.isBlank(menu.getRootId())) {
            menu.setRootId("0");
        }

        validateParent(menu);
        if (menu.getType() == MenuTypeEnum.MENU) {
            AssertUtils.isBlank(menu.getPath(), "菜单类型必须填写页面路径或链接");
            AssertUtils.isBlank(menu.getPermission(), "菜单类型必须填写页面权限标识");
            return;
        }

        if (menu.getType() == MenuTypeEnum.BUTTON) {
            AssertUtils.isBlank(menu.getPermission(), "按钮类型必须填写按钮权限标识");
            AssertUtils.isTrue(hasChildren(menu.getId()), "按钮权限不能作为目录父级");
            menu.setPath("");
            menu.setIcon(null);
            menu.setLocalIcon(null);
            menu.setVisible(false);
            menu.setIsIframe(false);
            return;
        }

        menu.setPermission("");
        menu.setIsIframe(false);
    }

    private void validateParent(SystemMenuDO menu) {
        if ("0".equals(menu.getRootId())) {
            return;
        }
        SystemMenuDO parent = this.getById(menu.getRootId());
        AssertUtils.isNull(parent, "上级菜单不存在");
        AssertUtils.isTrue(parent.getType() == MenuTypeEnum.BUTTON, "按钮权限不能作为上级菜单");
    }

    private boolean hasChildren(String menuId) {
        if (StrUtil.isBlank(menuId)) {
            return false;
        }
        return this.count(new LambdaQueryWrapper<SystemMenuDO>().eq(SystemMenuDO::getRootId, menuId)) > 0;
    }
}
