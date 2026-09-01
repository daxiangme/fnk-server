package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 系统字典类型请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemDictTypeAO {
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    private Boolean status = true;
    private String remark;
}
