package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

/**
 * 短信测试发送请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsSendAO {
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    @NotBlank(message = "手机号不能为空")
    private String mobile;

    private Map<String, String> params;
}
