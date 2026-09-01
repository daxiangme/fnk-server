package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统参数配置响应。
 *
 * @author Enigma
 */
@Data
public class InfraConfigVO {
    @Schema(description = "ID")
    private String id;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键")
    private String configKey;

    @Schema(description = "参数值")
    private String configValue;

    @Schema(description = "参数分组")
    private String groupCode;

    @Schema(description = "值类型")
    private String valueType;

    @Schema(description = "是否可见")
    private Boolean visible;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
