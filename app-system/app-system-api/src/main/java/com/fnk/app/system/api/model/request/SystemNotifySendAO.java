package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 站内信发送请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemNotifySendAO {
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    @NotEmpty(message = "接收用户不能为空")
    private List<String> userIds;

    private Map<String, String> params;
}
