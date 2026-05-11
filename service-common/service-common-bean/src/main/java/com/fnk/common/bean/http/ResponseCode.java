package com.fnk.common.bean.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Enigma
 */
@AllArgsConstructor
@Getter
public enum ResponseCode implements IResponseCode, Serializable {
    //成功
    OK(0, "成功"),
    //未登录授权
    AUTH_NOT_LOGIN(1, "未登录授权"),
    //无权限访问
    AUTH_NOT_PERMISSION(2, "无权限访问"),
    //服务器未知错误
    UNKNOWN_ERROR(3, "服务器未知错误"),
    //服务器未知错误
    REQ_METHOD_NOT_SUPPORT(4, "不支持的请求方式"),
    //逻辑业务异常
    LOGIC_FAIL(10, "逻辑业务异常"),
    //逻辑业务未找到
    LOGIC_NOT_FOUND(11, "逻辑业务未找到"),
    //参数不合法
    PARAMS_FAIL(12, "参数不合法"),
    //手机号码已注册
    LOGIC_HAS_REGISTER(100, "手机号码已注册"),
    //手机号码未注册
    LOGIC_NOT_REGISTER(101, "手机号码未注册"),
    ;

    /**
     * 业务码
     */
    private final int code;

    /**
     * 说明
     */
    private final String note;
}
