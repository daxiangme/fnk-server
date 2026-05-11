package com.fnk.common.bean.http;

/**
 * 接口响应码
 * @author Enigma
 */
public interface IResponseCode {
    /**
     * 获取业务码
     *
     * @return 业务码
     */
    int getCode();

    /**
     * 获取说明
     *
     * @return 说明
     */
    String getNote();
}
