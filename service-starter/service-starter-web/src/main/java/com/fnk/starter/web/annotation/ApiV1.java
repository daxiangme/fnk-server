package com.fnk.starter.web.annotation;

import com.fnk.starter.web.enums.Version;

import java.lang.annotation.*;

/**
 * @author Enigma
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiVersion(Version.V1)
public @interface ApiV1 {
}
