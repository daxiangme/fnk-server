package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

/**
 * 邮件测试发送请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailSendAO {
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    @NotBlank(message = "收件邮箱不能为空")
    @Email(message = "收件邮箱格式不正确")
    private String toMail;

    private Map<String, String> params;
}
