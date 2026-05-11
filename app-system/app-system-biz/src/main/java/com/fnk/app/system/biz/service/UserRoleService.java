package com.fnk.app.system.biz.service;

import com.fnk.common.db.impl.BaseService;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.entity.UserRoleDO;
import com.fnk.app.system.biz.dal.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return this.saveBatch(roleIds.stream().map(roleId -> new UserRoleDO(userId, roleId)).toList());
    }

    public int deleteByUserId(String userId) {
        return this.baseMapper.deleteByUserId(userId);
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


    public List<String> queryPermissionKeyByUserId(String userId) {
        List<String> permissionKeyList = this.baseMapper.queryPermissionKeyByUserId(userId);
        // 去重
        return permissionKeyList.stream().distinct().toList();
    }
}
