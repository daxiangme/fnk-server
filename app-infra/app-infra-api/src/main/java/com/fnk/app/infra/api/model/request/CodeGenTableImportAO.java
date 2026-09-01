package com.fnk.app.infra.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 导入数据表请求。
 *
 * @author Enigma
 */
@Data
public class CodeGenTableImportAO {
    @NotEmpty(message = "请选择需要导入的数据表")
    @Size(max = 100, message = "单次最多导入100张数据表")
    @Schema(description = "数据库表名列表")
    private List<@NotBlank(message = "表名不能为空") String> tableNames;
}
