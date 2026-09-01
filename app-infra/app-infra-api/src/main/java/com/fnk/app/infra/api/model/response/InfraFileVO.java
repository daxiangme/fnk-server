package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 文件资源响应。
 *
 * @author Enigma
 */
@Data
public class InfraFileVO {
    @Schema(description = "ID")
    private String id;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "存储文件名")
    private String fileName;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "文件配置ID")
    private String configId;

    @Schema(description = "文件配置名称")
    private String configName;

    @Schema(description = "存储路径")
    private String storagePath;

    @Schema(description = "访问地址")
    private String url;

    @Schema(description = "创建时间")
    private Date createTime;
}
