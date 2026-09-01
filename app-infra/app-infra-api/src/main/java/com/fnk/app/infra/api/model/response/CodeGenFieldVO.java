package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码生成字段配置。
 *
 * @author Enigma
 */
@Data
public class CodeGenFieldVO {
    private String id;
    private String tableId;

    @Schema(description = "数据库字段")
    private String columnName;

    @Schema(description = "属性名")
    private String propertyName;

    @Schema(description = "字段说明")
    private String columnComment;

    @Schema(description = "数据库类型")
    private String dbType;

    @Schema(description = "Java 类型")
    private String javaType;

    @Schema(description = "TypeScript 类型")
    private String tsType;

    @Schema(description = "是否主键")
    private Boolean primaryKey;

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

    @Schema(description = "表单控件")
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
