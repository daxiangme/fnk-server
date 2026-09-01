package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新代码生成表配置请求。
 *
 * @author Enigma
 */
@Data
public class CodeGenTableUpdateAO {
    @NotBlank(message = "业务名称不能为空")
    @Schema(description = "业务名称")
    private String businessName;

    @NotBlank(message = "模块名不能为空")
    @Schema(description = "模块名")
    private String moduleName;

    @NotBlank(message = "实体类名不能为空")
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
}
