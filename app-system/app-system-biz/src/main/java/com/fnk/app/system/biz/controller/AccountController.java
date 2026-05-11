package com.fnk.app.system.biz.controller;

import com.fnk.app.system.api.facade.AccountFacade;
import com.fnk.app.system.api.model.request.LoginAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.api.model.response.LoginVO;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.starter.security.annotation.AnonymousApi;
import com.fnk.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/account")
@Tag(name = "认证接口")
@AllArgsConstructor
@AnonymousApi
public class AccountController extends BaseController {
    private final AccountFacade accountFacade;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public RestResponse<LoginVO> create(@RequestBody @Validated LoginAO dto) {
        return RestResponse.ok(accountFacade.login(dto));
    }

    @GetMapping("/admin")
    @Operation(summary = "获取当前登录用户")
    public RestResponse<AdminUserVO> get() {
        return RestResponse.ok(accountFacade.currentAdmin(this.authId()));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public RestResponse<Void> logout() {
        accountFacade.logout(this.authId());
        return RestResponse.ok();
    }
}
