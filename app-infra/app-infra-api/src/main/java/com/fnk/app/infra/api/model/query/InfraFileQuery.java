package com.fnk.app.infra.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件资源查询。
 *
 * @author Enigma
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfraFileQuery extends SplitPageDTO {
    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "文件配置ID")
    private String configId;
}
