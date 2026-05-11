package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求。
 *
 * @author Enigma
 */
public record LoginAO(@NotBlank(message = "手机号码不能为空") String phone,
                      @NotBlank(message = "密码不能为空") String password,
                      String ip) {
}
