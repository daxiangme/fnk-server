package com.fnk.starter.web.annotation;

import com.fnk.starter.web.enums.Version;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * API版本控制
 * @author Enigma
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * API版本号
     *
     * @return 版本号
     */
    Version value();
}
