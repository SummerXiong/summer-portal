package com.summer.security.validate.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.validate.code.model
 * @date:2019/12/2
 */
@Data
public class ValidateCode {

    private String code;
    private LocalDateTime expire;

    public ValidateCode(String code, int expire) {
        this.code = code;
        this.expire = LocalDateTime.now().plusSeconds(expire);
    }

    public ValidateCode(String code, LocalDateTime expire) {
        this.code = code;
        this.expire = expire;
    }
}
