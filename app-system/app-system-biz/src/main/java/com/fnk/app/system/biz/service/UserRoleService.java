package com.fnk.app.system.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.entity.UserRoleDO;
import com.fnk.app.system.biz.dal.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 用户和角色关联 服务实现层
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Service
public class UserRoleService extends BaseService<UserRoleMapper, UserRoleDO> {


    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRole(String userId, List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }
        return this.saveBatch(roleIds.stream()
                .filter(Objects::nonNull)
                .filter(roleId -> !roleId.isBlank())
                .distinct()
                .map(roleId -> new UserRoleDO(userId, roleId))
                .toList());
    }

    public int deleteByUserId(String userId) {
        return this.baseMapper.deleteByUserId(userId);
    }

    public int deleteByRoleId(String roleId) {
        return deleteByRoleIds(List.of(roleId));
    }

    public int deleteByRoleIds(Collection<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return 0;
        }
        List<String> userIds = queryUserIdsByRoleIds(roleIds);
        boolean removed = this.remove(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getRoleId, roleIds));
        userIds.forEach(RoleCache::resetUserRoleCache);
        return removed ? userIds.size() : 0;
    }

    public int deleteByUserIds(Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }
        return this.remove(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getUserId, userIds)) ? userIds.size() : 0;
    }

    public List<String> queryRoleIdsByUserId(String userId) {
        return this.baseMapper.queryRoleIdsByUserId(userId);
    }

    public List<SystemMenuDO> queryMenusByUserId(String userId) {
        return this.baseMapper.queryMenusByUserId(userId);
    }

    public List<String> queryRoleKey(String userId) {
        return this.baseMapper.queryRoleKeyByUserId(userId);
    }

    public void resetUserRoleCacheByRoleId(String roleId) {
        resetUserRoleCacheByRoleIds(List.of(roleId));
    }

    public void resetUserRoleCacheByRoleIds(Collection<String> roleIds) {
        queryUserIdsByRoleIds(roleIds).forEach(RoleCache::resetUserRoleCache);
    }

    private List<String> queryUserIdsByRoleIds(Collection<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        return this.list(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getRoleId, roleIds))
                .stream()
                .map(UserRoleDO::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }


    public List<String> queryPermissionKeyByUserId(String userId) {
        List<String> permissionKeyList = this.baseMapper.queryPermissionKeyByUserId(userId);
        // 去重
        return permissionKeyList.stream()
                .filter(Objects::nonNull)
                .filter(permission -> !permission.isBlank())
                .distinct()
                .toList();
    }
}
