package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据库表信息。
 *
 * @author Enigma
 */
@Data
public class DatabaseTableVO {
    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableComment;

    @Schema(description = "表引擎")
    private String engine;

    @Schema(description = "字段数")
    private Integer columnCount;

    @Schema(description = "是否已导入")
    private Boolean imported;
}
