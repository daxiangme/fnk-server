package com.fnk.app.infra.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统参数配置查询。
 *
 * @author Enigma
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfraConfigQuery extends SplitPageDTO {
    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键")
    private String configKey;

    @Schema(description = "参数分组")
    private String groupCode;

    @Schema(description = "状态")
    private Boolean status;
}
