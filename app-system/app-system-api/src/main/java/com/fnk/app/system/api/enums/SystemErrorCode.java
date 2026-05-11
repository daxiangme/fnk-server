package com.fnk.app.system.api.enums;

import com.fnk.common.bean.http.IResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统模块错误码。
 *
 * @author Enigma
 */
@AllArgsConstructor
@Getter
public enum SystemErrorCode implements IResponseCode {
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_DISABLED(10002, "用户已禁用"),
    ROLE_NOT_FOUND(10101, "角色不存在"),
    MENU_NOT_FOUND(10201, "菜单不存在");

    private final int code;
    private final String note;
}
