package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 短信模板请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsTemplateAO {
    @NotBlank(message = "短信渠道不能为空")
    private String channelId;

    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    private String providerTemplateCode;
    private String templateType;

    @NotBlank(message = "模板内容不能为空")
    private String content;

    private List<String> params;
    private Boolean status = true;
    private String remark;
}
