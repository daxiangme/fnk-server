package com.fnk.app.infra.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成表配置查询。
 *
 * @author Enigma
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeGenTableQuery extends SplitPageDTO {
    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableComment;

    @Schema(description = "模块名")
    private String moduleName;

    @Schema(description = "生成类型")
    private String generateType;
}
