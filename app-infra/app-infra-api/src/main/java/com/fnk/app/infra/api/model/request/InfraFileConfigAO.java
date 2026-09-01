package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件配置请求。
 *
 * @author Enigma
 */
@Data
public class InfraFileConfigAO {
    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "存储类型：local/s3")
    private String storageType;

    @Schema(description = "是否主配置")
    private Boolean master;

    @Schema(description = "本地基础路径")
    private String basePath;

    @Schema(description = "访问域名")
    private String domain;

    @Schema(description = "最大上传大小，单位 MB")
    private Long maxSizeMb;

    @Schema(description = "S3 Endpoint")
    private String endpoint;

    @Schema(description = "S3 Bucket")
    private String bucket;

    @Schema(description = "S3 Access Key")
    private String accessKey;

    @Schema(description = "S3 Access Secret")
    private String accessSecret;

    @Schema(description = "是否启用 Path Style")
    private Boolean enablePathStyleAccess;

    @Schema(description = "备注")
    private String remark;
}
