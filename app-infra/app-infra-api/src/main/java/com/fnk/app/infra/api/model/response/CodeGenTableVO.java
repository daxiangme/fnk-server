package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 代码生成表配置。
 *
 * @author Enigma
 */
@Data
public class CodeGenTableVO {
    private String id;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableComment;

    @Schema(description = "业务名称")
    private String businessName;

    @Schema(description = "模块名")
    private String moduleName;

    @Schema(description = "实体类名")
    private String className;

    @Schema(description = "包路径")
    private String packageName;

    @Schema(description = "接口路径")
    private String apiBasePath;

    @Schema(description = "前端页面路径")
    private String frontendPath;

    @Schema(description = "路由路径")
    private String routePath;

    @Schema(description = "权限前缀")
    private String permissionPrefix;

    @Schema(description = "菜单父级ID")
    private String menuParentId;

    @Schema(description = "生成类型")
    private String generateType;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "最近同步时间")
    private Date syncTime;

    private Date createTime;

    private Date updateTime;
}
