package com.summer.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *
 *    SpringSecurity过滤器链
 *
 *         ----------------------请求--------------------------->
 *           UsernamePasswordAuthenticationFilter(用户表单登录)
 *           BasicAuthenticationFilter
 *
 *           ExceptionTranslationFilter
 *           FilterSecurityInterceptor (SpringSecurity是过滤器链的最后一环)
 *
 *         <---------------------响应----------------------------
 *
 *
 * @date:2019/11/28
 */
@SpringBootApplication
public class SecurityApplcation {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplcation.class, args);
    }
}
