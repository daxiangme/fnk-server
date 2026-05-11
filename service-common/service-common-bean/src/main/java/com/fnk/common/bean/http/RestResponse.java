package com.fnk.common.bean.http;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * @author Enigma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RestResponse", description = "接口响应")
@Slf4j
public class RestResponse<T> implements Serializable {
    @Schema(name = "msg", description = "响应消息")
    private String msg;
    @Schema(name = "code", description = "响应码")
    private int code;
    @Schema(name = "data", description = "响应数据")
    private T data;
    @Schema(name = "timestamp", description = "响应时间戳")
    private long timestamp;
    @Schema(name = "rid", description = "请求ID")
    private String rid;

    /**
     * 成功
     *
     * @return RestResponse
     */
    public static RestResponse<Void> ok() {
        return RestResponse.ok(null);
    }

    public static <T> RestResponse<T> ok(T data) {
        return RestResponse.ok(data, "ok");
    }

    public static <T> RestResponse<T> ok(T data, String msg) {
        return new RestResponse<>(msg, ResponseCode.OK.getCode(), data, System.currentTimeMillis(), getRequestId());
    }

    /**
     * 失败
     *
     * @param msg 错误说明
     */
    public static <T> RestResponse<T> fail(String msg) {
        return RestResponse.fail(msg, null);
    }

    public static <T> RestResponse<T> fail(String msg, T data) {
        return fail(msg, data, ResponseCode.LOGIC_FAIL);
    }

    public static <T> RestResponse<T> fail(IResponseCode code) {
        return fail(code.getNote(), null, code);
    }

    /**
     * 失败
     *
     * @param msg  错误说明
     * @param data 数据
     * @param code 错误码
     * @return RestResponse
     */
    public static <T> RestResponse<T> fail(String msg, T data, IResponseCode code) {
        return new RestResponse<>(msg, code.getCode(), data, System.currentTimeMillis(), getRequestId());
    }

    /**
     * 获取请求ID
     *
     * @return 请求ID
     */
    private static String getRequestId() {
        try {
            return MDC.get("requestId");
        } catch (Exception e) {
            log.error("failed for get request id", e);
        }
        return null;
    }
}
