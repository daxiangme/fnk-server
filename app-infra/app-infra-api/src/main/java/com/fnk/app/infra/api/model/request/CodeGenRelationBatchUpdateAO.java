package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量更新表关系配置请求。
 *
 * @author Enigma
 */
@Data
public class CodeGenRelationBatchUpdateAO {
    @Schema(description = "关系配置列表")
    private List<CodeGenRelationUpdateAO> relations;
}
