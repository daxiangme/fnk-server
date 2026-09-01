package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表关系配置更新项。
 *
 * @author Enigma
 */
@Data
public class CodeGenRelationUpdateAO {
    @Schema(description = "关系ID")
    private String id;

    @Schema(description = "关系名称")
    private String relationName;

    @Schema(description = "关系类型")
    private String relationType;

    @Schema(description = "当前表字段")
    private String sourceColumn;

    @Schema(description = "关联表")
    private String targetTable;

    @Schema(description = "关联字段")
    private String targetColumn;

    @Schema(description = "中间表")
    private String joinTable;

    @Schema(description = "中间表当前侧字段")
    private String joinSourceColumn;

    @Schema(description = "中间表目标侧字段")
    private String joinTargetColumn;

    @Schema(description = "展示字段")
    private String displayColumn;

    @Schema(description = "是否生成关联查询")
    private Boolean generateQuery;

    @Schema(description = "是否生成表单控件")
    private Boolean generateForm;

    @Schema(description = "是否生成详情")
    private Boolean generateDetail;

    @Schema(description = "删除策略")
    private String deleteStrategy;

    @Schema(description = "备注")
    private String remark;
}
