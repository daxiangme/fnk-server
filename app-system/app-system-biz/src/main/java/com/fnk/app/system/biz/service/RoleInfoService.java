package com.fnk.app.system.biz.service;

import com.fnk.common.db.impl.BaseService;
import com.fnk.common.tools.lang.AssertUtils;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.biz.dal.entity.RoleInfoDO;
import com.fnk.app.system.biz.dal.mapper.RoleInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(rollbackFor = Exception.class)
    public RoleInfoDO saveRole(RoleInfoDO roleInfo) {
        AssertUtils.isFalse(this.save(roleInfo), "保存角色信息失败");
        AssertUtils.isFalse(roleMenuService.saveRoleMenu(roleInfo.getId(), roleInfo.getRoleScope()), "保存角色菜单关联失败");
        return roleInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleInfoDO updateRole(RoleInfoDO roleInfo) {
        AssertUtils.isFalse(this.updateById(roleInfo), "更新角色信息失败");
        roleMenuService.deleteByRoleId(roleInfo.getId());
        AssertUtils.isFalse(roleMenuService.saveRoleMenu(roleInfo.getId(), roleInfo.getRoleScope()), "保存角色菜单关联失败");
        // 更新缓存
        RoleCache.setRolePermissionKey(roleInfo.getRoleKey(),roleMenuService.queryPermissionKeyByRoleKey(roleInfo.getRoleKey()));
        // 删除SaToken缓存
        RoleCache.deleteSaTokenRoleCache(roleInfo.getRoleKey());
        return roleInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleId(String roleId) {
        RoleInfoDO roleInfo =  this.getById(roleId);
        RoleCache.resetRoleCache(roleInfo.getRoleKey());
        RoleCache.deleteSaTokenRoleCache(roleInfo.getRoleKey());
        int count = roleMenuService.deleteByRoleId(roleId);
        return count + this.baseMapper.deleteById(roleId);
    }

    public RoleInfoDO queryDetail(String roleId) {
        RoleInfoDO roleInfo = this.getById(roleId);
        if (roleInfo != null) {
            roleInfo.setRoleScope(roleMenuService.queryMenuIdByRoleId(roleId));
            return roleInfo;
        }
        return null;
    }

    public List<String> queryRoleMenuIds(String roleId) {
        return roleMenuService.queryMenuIdByRoleId(roleId);
    }
}
