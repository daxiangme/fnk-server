package com.fnk.app.infra.api.constants;

/**
 * 基础服务权限常量。
 *
 * @author Enigma
 */
public final class InfraPermissionConstants {
    public static final String CODEGEN_VIEW = "infra:codegen:view";
    public static final String CODEGEN_IMPORT = "infra:codegen:import";
    public static final String CODEGEN_UPDATE = "infra:codegen:update";
    public static final String CODEGEN_PREVIEW = "infra:codegen:preview";
    public static final String CODEGEN_DOWNLOAD = "infra:codegen:download";
    public static final String CODEGEN_DELETE = "infra:codegen:delete";

    public static final String CONFIG_VIEW = "infra:config:view";
    public static final String CONFIG_CREATE = "infra:config:create";
    public static final String CONFIG_UPDATE = "infra:config:update";
    public static final String CONFIG_DELETE = "infra:config:delete";

    public static final String FILE_VIEW = "infra:file:view";
    public static final String FILE_UPLOAD = "infra:file:upload";
    public static final String FILE_DELETE = "infra:file:delete";

    public static final String FILE_CONFIG_VIEW = "infra:file-config:view";
    public static final String FILE_CONFIG_CREATE = "infra:file-config:create";
    public static final String FILE_CONFIG_UPDATE = "infra:file-config:update";
    public static final String FILE_CONFIG_DELETE = "infra:file-config:delete";
    public static final String FILE_CONFIG_MASTER = "infra:file-config:master";
    public static final String FILE_CONFIG_TEST = "infra:file-config:test";

    private InfraPermissionConstants() {
    }
}
