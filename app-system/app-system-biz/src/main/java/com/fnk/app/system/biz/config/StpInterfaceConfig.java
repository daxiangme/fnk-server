package com.fnk.app.system.biz.config;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.fnk.app.system.api.constants.SystemCacheKey;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.biz.service.RoleMenuService;
import com.fnk.app.system.biz.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口配置
 *
 * @author Enigma
 */

@Component
@Slf4j
public class StpInterfaceConfig implements StpInterface {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = new ArrayList<>();
        for (String roleKey : getRoleList(loginId, loginType)) {
            SaSession roleSession = SaSessionCustomUtil.getSessionById(SystemCacheKey.ROLE_SESSION_ID + roleKey);
            List<String> list = roleSession.get(SystemCacheKey.PERMISSION_SESSION_KEY, () -> {
                // 从数据库查询这个角色所拥有的权限列表
                List<String> permissionKeyList = RoleCache.getRolePermissionKey(roleKey);
                if (permissionKeyList == null) {
                    permissionKeyList = roleMenuService.queryPermissionKeyByRoleKey(roleKey);
                    RoleCache.setRolePermissionKey(roleKey, permissionKeyList);
                }
                return permissionKeyList;
            });
            permissionList.addAll(list);
        }

        // 3. 返回权限码集合
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        String userId = (String) loginId;
        return session.get(SystemCacheKey.ROLE_SESSION_KEY, () -> {
            // 从数据库查询这个账号id拥有的角色列表
            List<String> roleList = RoleCache.getUserRoleKey(userId);
            if (roleList == null) {
                roleList = userRoleService.queryRoleKey(userId);
                RoleCache.setUserRoleId(userId, roleList);
            }
            return roleList;
        });
    }
}
