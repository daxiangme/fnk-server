package com.fnk.app.system.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.constants.SystemPermissionConstants;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.tools.lang.AssertUtils;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.biz.dal.entity.RoleInfoDO;
import com.fnk.app.system.biz.dal.mapper.RoleInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 角色信息 服务实现层
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Service
@AllArgsConstructor
public class RoleInfoService extends BaseService<RoleInfoMapper, RoleInfoDO> {

    private RoleMenuService roleMenuService;
    private UserRoleService userRoleService;


    @Transactional(rollbackFor = Exception.class)
    public RoleInfoDO saveRole(RoleInfoDO roleInfo) {
        validateNormalRoleScope(roleInfo);
        AssertUtils.isFalse(this.save(roleInfo), "保存角色信息失败");
        List<String> roleScope = roleInfo.getRoleScope() == null ? Collections.emptyList() : roleInfo.getRoleScope();
        if (!roleScope.isEmpty()) {
            AssertUtils.isFalse(roleMenuService.saveRoleMenu(roleInfo.getId(), roleScope), "保存角色菜单关联失败");
        }
        return roleInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleInfoDO updateRole(RoleInfoDO roleInfo) {
        RoleInfoDO oldRoleInfo = this.getById(roleInfo.getId());
        AssertUtils.isNull(oldRoleInfo, "角色信息不存在");
        if (isSuperAdminRole(oldRoleInfo)) {
            AssertUtils.isTrue(Boolean.FALSE.equals(roleInfo.getStatus()), "系统内置超级管理员角色不能禁用");
            roleInfo.setRoleKey(SystemPermissionConstants.SUPER_ADMIN_ROLE_KEY);
            roleInfo.setRoleScope(List.of(SystemPermissionConstants.ALL_PERMISSION));
            roleInfo.setStatus(true);
            AssertUtils.isFalse(this.updateById(roleInfo), "更新角色信息失败");
            refreshRolePermissionCache(roleInfo.getRoleKey(), oldRoleInfo.getRoleKey());
            userRoleService.resetUserRoleCacheByRoleId(roleInfo.getId());
            return roleInfo;
        }
        validateNormalRoleScope(roleInfo);
        AssertUtils.isFalse(this.updateById(roleInfo), "更新角色信息失败");
        roleMenuService.deleteByRoleId(roleInfo.getId());
        List<String> roleScope = roleInfo.getRoleScope() == null ? Collections.emptyList() : roleInfo.getRoleScope();
        if (!roleScope.isEmpty()) {
            AssertUtils.isFalse(roleMenuService.saveRoleMenu(roleInfo.getId(), roleScope), "保存角色菜单关联失败");
        }
        refreshRolePermissionCache(roleInfo.getRoleKey(), oldRoleInfo == null ? null : oldRoleInfo.getRoleKey());
        userRoleService.resetUserRoleCacheByRoleId(roleInfo.getId());
        return roleInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleId(String roleId) {
        RoleInfoDO roleInfo =  this.getById(roleId);
        AssertUtils.isTrue(isSuperAdminRole(roleInfo), "系统内置超级管理员角色不能删除");
        RoleCache.resetRoleCache(roleInfo.getRoleKey());
        RoleCache.deleteSaTokenRoleCache(roleInfo.getRoleKey());
        int count = roleMenuService.deleteByRoleId(roleId);
        count += userRoleService.deleteByRoleId(roleId);
        return count + this.baseMapper.deleteById(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(Collection<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        List<RoleInfoDO> roleInfos = this.listByIds(roleIds);
        AssertUtils.isTrue(roleInfos.stream().anyMatch(this::isSuperAdminRole), "系统内置超级管理员角色不能删除");
        roleInfos.forEach(roleInfo -> {
            RoleCache.resetRoleCache(roleInfo.getRoleKey());
            RoleCache.deleteSaTokenRoleCache(roleInfo.getRoleKey());
            roleMenuService.deleteByRoleId(roleInfo.getId());
        });
        userRoleService.deleteByRoleIds(roleIds);
        AssertUtils.isFalse(this.removeBatchByIds(roleIds), "批量删除角色信息失败");
    }

    public RoleInfoDO queryDetail(String roleId) {
        RoleInfoDO roleInfo = this.getById(roleId);
        if (roleInfo != null) {
            roleInfo.setRoleScope(isSuperAdminRole(roleInfo)
                    ? List.of(SystemPermissionConstants.ALL_PERMISSION)
                    : roleMenuService.queryMenuIdByRoleId(roleId));
            return roleInfo;
        }
        return null;
    }

    public List<String> queryRoleMenuIds(String roleId) {
        RoleInfoDO roleInfo = this.getById(roleId);
        if (isSuperAdminRole(roleInfo)) {
            return List.of(SystemPermissionConstants.ALL_PERMISSION);
        }
        return roleMenuService.queryMenuIdByRoleId(roleId);
    }

    public boolean hasWildcardPermission(String roleKey) {
        if (roleKey == null || roleKey.isBlank()) {
            return false;
        }
        RoleInfoDO roleInfo = this.getFirst(new LambdaQueryWrapper<RoleInfoDO>()
                .eq(RoleInfoDO::getRoleKey, roleKey)
                .eq(RoleInfoDO::getStatus, true));
        return isSuperAdminRole(roleInfo) && hasWildcardScope(roleInfo.getRoleScope());
    }

    public boolean hasWildcardPermission(Collection<String> roleKeys) {
        if (roleKeys == null || roleKeys.isEmpty()) {
            return false;
        }
        return roleKeys.stream().anyMatch(this::hasWildcardPermission);
    }

    private void refreshRolePermissionCache(String roleKey, String oldRoleKey) {
        if (oldRoleKey != null && !oldRoleKey.equals(roleKey)) {
            RoleCache.resetRoleCache(oldRoleKey);
            RoleCache.deleteSaTokenRoleCache(oldRoleKey);
        }
        if (SystemPermissionConstants.SUPER_ADMIN_ROLE_KEY.equals(roleKey)) {
            RoleCache.resetRoleCache(roleKey);
            RoleCache.deleteSaTokenRoleCache(roleKey);
            return;
        }
        RoleCache.setRolePermissionKey(roleKey, roleMenuService.queryPermissionKeyByRoleKey(roleKey));
        RoleCache.deleteSaTokenRoleCache(roleKey);
    }

    private void validateNormalRoleScope(RoleInfoDO roleInfo) {
        AssertUtils.isTrue(SystemPermissionConstants.SUPER_ADMIN_ROLE_KEY.equals(roleInfo.getRoleKey()), "系统内置超级管理员角色不能手动创建或修改");
        AssertUtils.isTrue(hasWildcardScope(roleInfo.getRoleScope()), "只有系统内置超级管理员角色允许配置 * 权限");
    }

    private boolean isSuperAdminRole(RoleInfoDO roleInfo) {
        return roleInfo != null && SystemPermissionConstants.SUPER_ADMIN_ROLE_KEY.equals(roleInfo.getRoleKey());
    }

    private boolean hasWildcardScope(List<String> roleScope) {
        return roleScope != null && roleScope.contains(SystemPermissionConstants.ALL_PERMISSION);
    }
}
