package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成字段配置。
 *
 * @author Enigma
 */
@Data
@TableName("infra_codegen_field")
@EqualsAndHashCode(callSuper = true)
public class CodeGenFieldDO extends BaseEntity<CodeGenFieldDO> {
    private String tableId;
    private String columnName;
    private String propertyName;
    private String columnComment;
    private String dbType;
    private String javaType;
    private String tsType;
    private Boolean primaryKey;
    private Boolean required;
    private Boolean listVisible;
    private Boolean searchVisible;
    private Boolean formVisible;
    private Boolean detailVisible;
    private String formType;
    private String queryType;
    private String dictCode;
    private String defaultValue;
    private Integer orderSort;
    private Integer width;
    private Boolean readonlyOnCreate;
    private Boolean readonlyOnEdit;
}
