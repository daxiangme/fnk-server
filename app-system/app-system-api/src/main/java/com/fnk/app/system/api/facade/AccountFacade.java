package com.fnk.app.system.api.facade;

import com.fnk.app.system.api.model.request.LoginAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.api.model.response.LoginVO;

/**
 * 认证 facade。
 *
 * @author Enigma
 */
public interface AccountFacade {
    LoginVO login(LoginAO login);

    AdminUserVO currentAdmin(String userId);

    void logout(String userId);
}
