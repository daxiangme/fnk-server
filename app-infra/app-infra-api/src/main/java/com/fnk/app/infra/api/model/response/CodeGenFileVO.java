package com.fnk.app.infra.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码预览文件。
 *
 * @author Enigma
 */
@Data
public class CodeGenFileVO {
    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件内容")
    private String content;
}
