package com.fnk.app.system.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.app.system.biz.dal.entity.RoleMenuDO;
import com.fnk.app.system.biz.dal.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
* 角色和菜单关联 服务实现层
*
* @author Enigma
* @since 2023-12-18
*/
@Service
public class RoleMenuService extends BaseService<RoleMenuMapper, RoleMenuDO> {


    @Transactional(rollbackFor = Exception.class)
    public int deleteByMenuId(String menuId) {
        return this.baseMapper.deleteByMenuId(menuId);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByMenuIds(Collection<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return 0;
        }
        return this.remove(new LambdaQueryWrapper<RoleMenuDO>().in(RoleMenuDO::getMenuId, menuIds)) ? menuIds.size() : 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleId(String roleId) {
        return this.baseMapper.deleteByRoleId(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleMenu(String roleId, List<String> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return true;
        }
        return this.saveBatch(menuIds.stream()
                .filter(Objects::nonNull)
                .filter(menuId -> !menuId.isBlank())
                .distinct()
                .map(menuId -> new RoleMenuDO(roleId, menuId))
                .toList());
    }

    public List<String> queryMenuIdByRoleId(String roleId) {
        return this.baseMapper.queryMenuIdByRoleId(roleId);
    }

    public List<String> queryPermissionKeyByRoleId(String roleId) {
        return this.baseMapper.queryPermissionKeyByRoleId(roleId);
    }

    public List<String> queryPermissionKeyByRoleKey(String roleKey) {
        List<String> permissionKeyList = this.baseMapper.queryPermissionKeyByRoleKey(roleKey);
        return permissionKeyList.stream()
                .filter(Objects::nonNull)
                .filter(permission -> !permission.isBlank())
                .distinct()
                .toList();
    }
}
