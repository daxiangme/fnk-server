package com.fnk.starter.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匿名接口，提供SaToken中缺失的匿名注解访问
 * @author Enigma
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD,})
public @interface AnonymousApi {
}
