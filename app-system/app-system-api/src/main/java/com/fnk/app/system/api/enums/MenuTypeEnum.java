package com.fnk.app.system.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统菜单类型。
 *
 * @author Enigma
 */
@AllArgsConstructor
@Getter
public enum MenuTypeEnum {
    TABLE("TABLE", "目录"),
    MENU("MENU", "菜单"),
    BUTTON("BUTTON", "按钮");

    private final String value;
    private final String desc;
}
