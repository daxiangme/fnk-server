package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字段映射更新项。
 *
 * @author Enigma
 */
@Data
public class CodeGenFieldUpdateAO {
    @NotBlank(message = "字段ID不能为空")
    @Schema(description = "字段ID")
    private String id;

    @Schema(description = "属性名")
    private String propertyName;

    @Schema(description = "字段说明")
    private String columnComment;

    @Schema(description = "Java 类型")
    private String javaType;

    @Schema(description = "TypeScript 类型")
    private String tsType;

    @Schema(description = "是否必填")
    private Boolean required;

    @Schema(description = "列表显示")
    private Boolean listVisible;

    @Schema(description = "搜索显示")
    private Boolean searchVisible;

    @Schema(description = "表单显示")
    private Boolean formVisible;

    @Schema(description = "详情显示")
    private Boolean detailVisible;

    @Schema(description = "表单控件类型")
    private String formType;

    @Schema(description = "查询方式")
    private String queryType;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "排序")
    private Integer orderSort;

    @Schema(description = "列宽")
    private Integer width;

    @Schema(description = "新增只读")
    private Boolean readonlyOnCreate;

    @Schema(description = "编辑只读")
    private Boolean readonlyOnEdit;
}
