package com.fnk.starter.web.enums;

import com.fnk.starter.web.interf.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Enigma
 */
@AllArgsConstructor
@Getter
public enum GenderType implements IBaseEnum<String> {

    // 性别 男 女
    MAN("0"),
    WOMAN("1"),
    OTHER("2");

    private final String value;
}
