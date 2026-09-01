package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 邮箱账号请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailAccountAO {
    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱地址格式不正确")
    private String mail;

    @NotBlank(message = "SMTP 用户名不能为空")
    private String username;

    private String password;

    @NotBlank(message = "SMTP 主机不能为空")
    private String host;

    @NotNull(message = "SMTP 端口不能为空")
    private Integer port;

    private Boolean sslEnable = true;
    private Boolean starttlsEnable = false;
    private Boolean status = true;
    private String remark;
}
