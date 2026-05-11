package com.fnk.starter.web.enums;

import com.fnk.starter.web.annotation.ApiV1;
import com.fnk.starter.web.annotation.ApiV2;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * 版本
 * @author Enigma
 */
@AllArgsConstructor
@Getter
public enum Version {
    //版本
    V1(1, "V1", ApiV1.class),
    V2(2, "V2", ApiV2.class);

    private final Integer version;

    private final String name;

    private final Class<? extends Annotation> aClass;
}
