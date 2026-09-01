package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 系统字典项请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemDictItemAO {
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @NotBlank(message = "字典标签不能为空")
    private String label;

    @NotBlank(message = "字典值不能为空")
    private String value;

    private Integer orderSort = 0;
    private Boolean status = true;
    private String tagType;
    private String remark;
}
