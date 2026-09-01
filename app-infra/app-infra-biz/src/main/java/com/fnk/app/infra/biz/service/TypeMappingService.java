package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 数据库字段到后端、前端和控件的类型映射。
 *
 * @author Enigma
 */
@Service
public class TypeMappingService {

    public MappingResult map(String columnName, String dataType, String columnType, boolean primaryKey) {
        String normalizedDataType = StrUtil.blankToDefault(dataType, "").toLowerCase(Locale.ROOT);
        String normalizedColumnType = StrUtil.blankToDefault(columnType, "").toLowerCase(Locale.ROOT);
        String normalizedColumnName = StrUtil.blankToDefault(columnName, "").toLowerCase(Locale.ROOT);

        String javaType = "String";
        String tsType = "string";
        String formType = "input";
        String queryType = "like";

        if (primaryKey || "id".equals(normalizedColumnName) || normalizedColumnName.endsWith("_id")) {
            javaType = primaryKey ? "String" : "Long";
            tsType = primaryKey ? "string" : "number";
            formType = "input";
            queryType = "eq";
        } else if (isInteger(normalizedDataType)) {
            javaType = "Integer";
            tsType = "number";
            formType = "number";
            queryType = "eq";
        } else if ("bigint".equals(normalizedDataType)) {
            javaType = "Long";
            tsType = "number";
            formType = "number";
            queryType = "eq";
        } else if (isDecimal(normalizedDataType)) {
            javaType = "BigDecimal";
            tsType = "number";
            formType = "number";
            queryType = "eq";
        } else if (isBoolean(normalizedDataType, normalizedColumnType)) {
            javaType = "Boolean";
            tsType = "boolean";
            formType = "switch";
            queryType = "eq";
        } else if ("date".equals(normalizedDataType)) {
            javaType = "LocalDate";
            tsType = "string";
            formType = "date";
            queryType = "between";
        } else if ("datetime".equals(normalizedDataType) || "timestamp".equals(normalizedDataType)) {
            javaType = "LocalDateTime";
            tsType = "string";
            formType = "datetime";
            queryType = "between";
        } else if (isText(normalizedDataType)) {
            formType = "textarea";
        } else if ("json".equals(normalizedDataType)) {
            tsType = "any";
            formType = "textarea";
        }

        if (normalizedColumnName.contains("status")) {
            formType = "switch";
            queryType = "eq";
        } else if (normalizedColumnName.contains("type")) {
            formType = "dict-select";
            queryType = "eq";
        } else if (normalizedColumnName.contains("remark") || normalizedColumnName.contains("content")) {
            formType = "textarea";
        } else if (normalizedColumnName.contains("file")
                || normalizedColumnName.contains("image")
                || normalizedColumnName.contains("avatar")) {
            formType = "upload";
        }

        return new MappingResult(javaType, tsType, formType, queryType);
    }

    private boolean isInteger(String dataType) {
        return "int".equals(dataType)
                || "integer".equals(dataType)
                || "smallint".equals(dataType)
                || "mediumint".equals(dataType);
    }

    private boolean isDecimal(String dataType) {
        return "decimal".equals(dataType)
                || "numeric".equals(dataType)
                || "double".equals(dataType)
                || "float".equals(dataType);
    }

    private boolean isBoolean(String dataType, String columnType) {
        return "bit".equals(dataType) || ("tinyint".equals(dataType) && columnType.contains("(1)"));
    }

    private boolean isText(String dataType) {
        return "text".equals(dataType)
                || "mediumtext".equals(dataType)
                || "longtext".equals(dataType)
                || "tinytext".equals(dataType);
    }

    @Data
    @AllArgsConstructor
    public static class MappingResult {
        private String javaType;
        private String tsType;
        private String formType;
        private String queryType;
    }
}
