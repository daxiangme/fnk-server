package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 短信渠道请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsChannelAO {
    @NotBlank(message = "渠道名称不能为空")
    private String channelName;

    @NotBlank(message = "渠道编码不能为空")
    private String channelCode;

    private String accessKey;
    private String accessSecret;
    private String signature;
    private String endpoint;
    private Boolean status = true;
    private String remark;
}
