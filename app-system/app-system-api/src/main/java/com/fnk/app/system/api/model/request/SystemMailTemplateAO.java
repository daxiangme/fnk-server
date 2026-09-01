package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 邮件模板请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailTemplateAO {
    @NotBlank(message = "邮箱账号不能为空")
    private String accountId;

    @NotBlank(message = "模板名称不能为空")
    private String name;

    @NotBlank(message = "模板编码不能为空")
    private String code;

    private String fromName;

    @NotBlank(message = "邮件标题不能为空")
    private String title;

    @NotBlank(message = "邮件内容不能为空")
    private String content;

    private List<String> params;
    private Boolean status = true;
    private String remark;
}
