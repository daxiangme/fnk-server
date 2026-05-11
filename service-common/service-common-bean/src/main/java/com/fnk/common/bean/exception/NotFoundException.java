package com.fnk.common.bean.exception;

import lombok.EqualsAndHashCode;

import javax.security.auth.login.LoginException;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends LoginException {
    private String message;
}
