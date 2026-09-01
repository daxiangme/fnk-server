package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 站内信模板请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemNotifyTemplateAO {
    @NotBlank(message = "模板名称不能为空")
    private String name;

    @NotBlank(message = "模板编码不能为空")
    private String code;

    @NotBlank(message = "发送人昵称不能为空")
    private String nickname;

    private String templateType;

    @NotBlank(message = "模板内容不能为空")
    private String content;

    private List<String> params;
    private Boolean status = true;
    private String remark;
}
