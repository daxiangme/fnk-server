package com.fnk.app.system.api.constants;

/**
 * 系统模块缓存 key。
 *
 * @author Enigma
 */
public class SystemCacheKey {
    private SystemCacheKey() {
    }

    public static final String ROLE_USER = "role:user:";
    public static final String ROLE_PERMISSION = "role:permission:";
    public static final String ROLE_SESSION_ID = "role:";
    public static final String ROLE_SESSION_KEY = "role";
    public static final String PERMISSION_SESSION_KEY = "permission";
    public static final String AUTH_CUSTOM_SESSION_KEY = "satoken:custom:role:";
}
