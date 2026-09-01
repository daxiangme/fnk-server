package com.fnk.app.infra.api.model.response;

import lombok.Data;

import java.util.List;

/**
 * 代码生成预览。
 *
 * @author Enigma
 */
@Data
public class CodeGenPreviewVO {
    private CodeGenTableVO table;
    private List<CodeGenFieldVO> fields;
    private List<CodeGenRelationVO> relations;
    private List<CodeGenFileVO> files;
}
