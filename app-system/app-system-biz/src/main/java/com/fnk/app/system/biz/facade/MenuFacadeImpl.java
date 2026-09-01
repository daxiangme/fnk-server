package com.fnk.app.system.biz.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.facade.MenuFacade;
import com.fnk.app.system.api.model.query.MenuQuery;
import com.fnk.app.system.api.model.request.MenuCreateAO;
import com.fnk.app.system.api.model.request.MenuUpdateAO;
import com.fnk.app.system.api.model.response.SystemMenuVO;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.service.SystemMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单 facade 实现。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class MenuFacadeImpl implements MenuFacade {
    private final SystemMenuService systemMenuService;

    @Override
    public List<SystemMenuVO> list(MenuQuery query) {
        LambdaQueryWrapper<SystemMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, SystemMenuDO::getName, query.getName());
        wrapper.eq(query.getPermission() != null, SystemMenuDO::getPermission, query.getPermission());
        wrapper.orderByAsc(SystemMenuDO::getOrderSort);
        return SystemConvert.toSystemMenuVOList(systemMenuService.list(wrapper));
    }

    @Override
    public List<SystemMenuVO> listByRootId(String rootId) {
        return SystemConvert.toSystemMenuVOList(systemMenuService.listByRootId(rootId));
    }

    @Override
    public SystemMenuVO detail(String id) {
        return SystemConvert.toSystemMenuVO(systemMenuService.detail(id));
    }

    @Override
    public SystemMenuVO create(MenuCreateAO req) {
        SystemMenuVO result = SystemConvert.toSystemMenuVO(systemMenuService.create(SystemConvert.toSystemMenuDO(req)));
        RoleCache.resetAllPermissionCaches();
        return result;
    }

    @Override
    public SystemMenuVO update(String id, MenuUpdateAO req) {
        SystemMenuVO result = SystemConvert.toSystemMenuVO(systemMenuService.update(id, SystemConvert.toSystemMenuDO(req)));
        RoleCache.resetAllPermissionCaches();
        return result;
    }

    @Override
    public void remove(String id) {
        systemMenuService.deleteMenu(id);
        RoleCache.resetAllPermissionCaches();
    }

    @Override
    public void remove(List<String> idList) {
        systemMenuService.deleteMenus(idList);
        RoleCache.resetAllPermissionCaches();
    }

    @Override
    public void refreshPermissionCache() {
        RoleCache.resetAllPermissionCaches();
    }
}
