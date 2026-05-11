package com.fnk.common.bean.exception;

import com.fnk.common.bean.http.IResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogicException extends RuntimeException{
    private String message;

    private IResponseCode code;

    public LogicException(String message) {
        this.message = message;
    }

    public LogicException(IResponseCode code) {
        this.code = code;
    }

    public LogicException(String message, IResponseCode code) {
        this.message = message;
        this.code = code;
    }
}
