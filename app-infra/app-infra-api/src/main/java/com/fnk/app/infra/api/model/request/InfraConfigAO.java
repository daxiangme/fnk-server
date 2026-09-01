package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 系统参数配置请求。
 *
 * @author Enigma
 */
@Data
public class InfraConfigAO {
    @Schema(description = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    private String configName;

    @Schema(description = "参数键")
    @NotBlank(message = "参数键不能为空")
    private String configKey;

    @Schema(description = "参数值")
    private String configValue;

    @Schema(description = "参数分组")
    @NotBlank(message = "参数分组不能为空")
    private String groupCode;

    @Schema(description = "值类型")
    private String valueType;

    @Schema(description = "是否可见")
    private Boolean visible = true;

    @Schema(description = "状态")
    private Boolean status = true;

    @Schema(description = "备注")
    private String remark;
}
