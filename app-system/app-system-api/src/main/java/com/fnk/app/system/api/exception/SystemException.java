package com.fnk.app.system.api.exception;

import com.fnk.app.system.api.enums.SystemErrorCode;
import com.fnk.common.bean.exception.LogicException;

/**
 * 系统模块业务异常。
 *
 * @author Enigma
 */
public class SystemException extends LogicException {
    public SystemException(SystemErrorCode code) {
        super(code);
    }

    public SystemException(String message, SystemErrorCode code) {
        super(message, code);
    }
}
