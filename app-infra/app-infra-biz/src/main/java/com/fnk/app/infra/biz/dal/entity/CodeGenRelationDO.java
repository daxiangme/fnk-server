package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成表关系。
 *
 * @author Enigma
 */
@Data
@TableName("infra_codegen_relation")
@EqualsAndHashCode(callSuper = true)
public class CodeGenRelationDO extends BaseEntity<CodeGenRelationDO> {
    private String tableId;
    private String relationName;
    private String relationType;
    private String sourceTable;
    private String sourceColumn;
    private String targetTable;
    private String targetColumn;
    private String joinTable;
    private String joinSourceColumn;
    private String joinTargetColumn;
    private String displayColumn;
    private Boolean generateQuery;
    private Boolean generateForm;
    private Boolean generateDetail;
    private String deleteStrategy;
    private Integer confidence;
    private String sourceType;
    private String remark;
}
