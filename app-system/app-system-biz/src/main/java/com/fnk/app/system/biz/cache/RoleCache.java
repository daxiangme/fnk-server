package com.fnk.app.system.biz.cache;

import cn.hutool.json.JSONUtil;
import com.fnk.app.system.api.constants.SystemCacheKey;
import com.fnk.common.tools.utils.RedisUtils;

import java.util.List;

/**
 * 角色相关缓存
 *
 * @author Enigma
 */
public class RoleCache {

    /**
     * 获取用户角色keys
     *
     * @param userId
     * @return
     */
    public static List<String> getUserRoleKey(String userId) {
        Object result = RedisUtils.getINSTANCE().get(SystemCacheKey.ROLE_USER + userId);
        if (result != null) {
            return JSONUtil.toList(JSONUtil.parseArray(result), String.class);
        }
        return null;
    }

    /**
     * 缓存用户角色key
     *
     * @param userId  用户id
     * @param roleKeys 角色keys
     * @return
     */
    public static boolean setUserRoleId(String userId, List<String> roleKeys) {
        return RedisUtils.getINSTANCE().set(SystemCacheKey.ROLE_USER + userId, JSONUtil.toJsonStr(roleKeys));
    }

    /**
     * 删除用户角色缓存
     *
     * @param userId
     */
    public static void resetUserRoleCache(String userId) {
        RedisUtils.getINSTANCE().del(SystemCacheKey.ROLE_USER + userId);
    }

    public static List<String> getRolePermissionKey(String roleKey) {
        Object result = RedisUtils.getINSTANCE().get(SystemCacheKey.ROLE_PERMISSION + roleKey);
        if (result != null) {
            return JSONUtil.toList(JSONUtil.parseArray(result), String.class);
        }
        return null;
    }

    /**
     * 缓存角色权限id
     *
     * @param roleKey        角色key
     * @param permissionKeys 权限key
     * @return
     */
    public static boolean setRolePermissionKey(String roleKey, List<String> permissionKeys) {
        return RedisUtils.getINSTANCE().set(SystemCacheKey.ROLE_PERMISSION + roleKey, JSONUtil.toJsonStr(permissionKeys));
    }

    /**
     * 删除角色缓存
     *
     * @param roleKey
     */
    public static void resetRoleCache(String roleKey) {
        RedisUtils.getINSTANCE().del(SystemCacheKey.ROLE_PERMISSION + roleKey);
    }

    /**
     * 删除SaToken角色缓存
     * @param roleKey 角色key
     */
    public static void deleteSaTokenRoleCache(String roleKey) {
        RedisUtils.getINSTANCE().del(SystemCacheKey.AUTH_CUSTOM_SESSION_KEY + roleKey);
    }
}
