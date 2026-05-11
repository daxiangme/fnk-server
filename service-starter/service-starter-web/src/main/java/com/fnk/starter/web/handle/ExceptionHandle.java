package com.fnk.starter.web.handle;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.fnk.common.bean.exception.AuthException;
import com.fnk.common.bean.exception.LogicException;
import com.fnk.common.bean.exception.NotFoundException;
import com.fnk.common.bean.exception.NotHasPermissionException;
import com.fnk.common.bean.http.ResponseCode;
import com.fnk.common.bean.http.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author Enigma
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ExceptionHandler(value = LogicException.class)
    public RestResponse<String> logic(LogicException e) {
        if (e.getCode() != null) {
            return RestResponse.fail(e.getCode());
        }
        return RestResponse.fail(e.getMessage());
    }

    @ExceptionHandler(value = {NotLoginException.class, AuthException.class})
    public RestResponse<String> notLogin(HttpServletResponse res) {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        return RestResponse.fail(ResponseCode.AUTH_NOT_LOGIN);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestResponse<String> requestMethodNotSupport(HttpRequestMethodNotSupportedException e) {
        return RestResponse.fail(ResponseCode.REQ_METHOD_NOT_SUPPORT);
    }

    @ExceptionHandler(value = {NotPermissionException.class, NotRoleException.class, NotHasPermissionException.class})
    public RestResponse<String> notPermission(HttpServletResponse res) {
        res.setStatus(HttpStatus.FORBIDDEN.value());
        return RestResponse.fail(ResponseCode.AUTH_NOT_PERMISSION);
    }
    @ExceptionHandler(value = NotFoundException.class)
    public RestResponse<String> notFound(HttpServletResponse res, NotFoundException e) {
        res.setStatus(HttpStatus.NOT_FOUND.value());
        return RestResponse.fail(e.getMessage(), null, ResponseCode.LOGIC_NOT_FOUND);
    }

    @ExceptionHandler(value = {BindException.class})
    public RestResponse<String> argumentNotValidFail(HttpServletResponse res, BindException e) {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        BindingResult result = e.getBindingResult();
        String errMessage = "参数验证失败";
        if (result.hasErrors()) {
            errMessage = result.getAllErrors().get(0).getDefaultMessage();
        }
        return RestResponse.fail(errMessage, null, ResponseCode.PARAMS_FAIL);
    }
    @ExceptionHandler(value = {PersistenceException.class})
    public RestResponse<String> argumentNotValidFail(HttpServletResponse res, PersistenceException e) {
        return RestResponse.fail("数据库错误", null, ResponseCode.PARAMS_FAIL);
    }


    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse<String> paramsBad(Exception e) {
        //todo 此处线上应屏蔽错误信息
        return RestResponse.fail("无效的参数请求", e.getMessage(), ResponseCode.PARAMS_FAIL);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<String> unknown(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return RestResponse.fail("未知异常", e.getMessage(), ResponseCode.UNKNOWN_ERROR);
    }
}
