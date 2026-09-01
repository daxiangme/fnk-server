package com.fnk.app.infra.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件配置查询。
 *
 * @author Enigma
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfraFileConfigQuery extends SplitPageDTO {
    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "是否主配置")
    private Boolean master;
}
