package com.summer.security.sms.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.sms.exception
 * @date:2019/12/2
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
