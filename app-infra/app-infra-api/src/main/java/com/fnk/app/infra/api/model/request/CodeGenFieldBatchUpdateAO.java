package com.fnk.app.infra.api.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量更新字段映射请求。
 *
 * @author Enigma
 */
@Data
public class CodeGenFieldBatchUpdateAO {
    @Valid
    @NotEmpty(message = "字段配置不能为空")
    private List<CodeGenFieldUpdateAO> fields;
}
