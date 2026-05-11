package com.fnk.starter.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * swagger 枚举文档扩展
 * @author Enigma
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerEnumDoc {
    /**
     * 索引字段
     *
     * @return 索引字段
     */
    String index() default "index";

    /**
     * 说明字段
     *
     * @return 说明字段
     */
    String desc() default "desc";
}
