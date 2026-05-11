package com.fnk.app.system.biz.facade;

import com.fnk.app.system.api.facade.AccountFacade;
import com.fnk.app.system.api.model.request.LoginAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.api.model.response.LoginVO;
import com.fnk.app.system.biz.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 认证 facade 实现。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class AccountFacadeImpl implements AccountFacade {
    private final AdminUserService adminUserService;

    @Override
    public LoginVO login(LoginAO login) {
        return adminUserService.login(login);
    }

    @Override
    public AdminUserVO currentAdmin(String userId) {
        return adminUserService.getCurrentAdminInfo(userId);
    }

    @Override
    public void logout(String userId) {
        adminUserService.logout(userId);
    }
}
