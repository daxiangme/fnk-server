package com.fnk.starter.web.annotation;

import java.lang.annotation.*;

/**
 * @author Enigma
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 日志名称
     * 若不存在会自动以@ApiOperation的释义为准
     *
     * @return 名称
     */
    String value() default "";

    /**
     * 记录IP
     * 默认为true
     *
     * @return 与否
     */
    boolean ip() default true;

    /**
     * 记录请求体
     * 默认为true
     *
     * @return 与否
     */
    boolean reqBody() default true;

    /**
     * 记录响应体
     * 默认为false
     *
     * @return 与否
     */
    boolean resBody() default false;

    /**
     * 移除敏感信息
     * SI = Sensitive Information
     * 默认为false
     *
     * @return 与否
     */
    boolean removeSI() default false;

    /**
     * 记录异常执行
     * 默认为false
     * @return 与否
     */
    boolean exception() default false;
}
