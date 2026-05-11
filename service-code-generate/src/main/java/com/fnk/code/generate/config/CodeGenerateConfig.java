package com.fnk.code.generate.config;

import lombok.Data;

import java.util.List;

/**
 * 代码生成配置
 * @author Enigma
 */
@Data
public class CodeGenerateConfig {
    /**
     * 指定表名称集合
     */
    private List<String> tables;

    /**
     * 数据库信息
     */
    private CodeGenerateConfigDb db;

    /**
     * 作者
     */
    private String author;

    /**
     * 生成根目录
     */
    private String output;

    /**
     * 包配置
     */
    private CodeGenerateConfigPackage pkg;

    /**
     * 是否开启swagger支持
     */
    private boolean swagger;

    @Data
    public static class CodeGenerateConfigDb {
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class CodeGenerateConfigPackage {
        /**
         * 父级包名
         */
        private String parent;
        /**
         * 模块名
         */
        private String module;
    }
}
