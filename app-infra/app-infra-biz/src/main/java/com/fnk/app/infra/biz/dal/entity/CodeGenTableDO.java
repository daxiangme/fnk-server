package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 代码生成表配置。
 *
 * @author Enigma
 */
@Data
@TableName("infra_codegen_table")
@EqualsAndHashCode(callSuper = true)
public class CodeGenTableDO extends BaseEntity<CodeGenTableDO> {
    private String tableName;
    private String tableComment;
    private String businessName;
    private String moduleName;
    private String className;
    private String packageName;
    private String apiBasePath;
    private String frontendPath;
    private String routePath;
    private String permissionPrefix;
    private String menuParentId;
    private String generateType;
    private String author;
    private Date syncTime;
}
