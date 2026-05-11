package com.fnk.app.system.api.model.response;

import lombok.Data;

/**
 * 登录响应。
 *
 * @author Enigma
 */
@Data
public class LoginVO {
    private String tokenName;
    private String tokenValue;
    private Boolean isLogin;
    private Object loginId;
    private String loginType;
    private Long tokenTimeout;
    private Long sessionTimeout;
    private Long tokenSessionTimeout;
    private Long tokenActivityTimeout;
    private String loginDevice;
}
