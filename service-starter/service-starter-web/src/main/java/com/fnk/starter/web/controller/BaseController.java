package com.fnk.starter.web.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;

/**
 * @author Enigma
 */
public class BaseController {
    /**
     * 获取当前授权用户的信息
     *
     * @return 用户授权信息
     */
    public SaTokenInfo authInfo() {
        return StpUtil.getTokenInfo();
    }

    /**
     * 获取当前授权用户的ID
     *
     * @return 用户ID
     */
    public String authId() {
        Object loginId = StpUtil.getLoginId();
        if (loginId instanceof String s) {
            return s;
        }
        return null;
    }

    public void authLogout() {
        StpUtil.logout();
    }

}
