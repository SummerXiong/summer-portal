package com.summer.security.controller;

import com.summer.security.validate.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:  验证码Controller
 *
 * @date:2019/12/2
 */
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

    @Autowired
    protected ValidateCodeProcessor validateCodeProcessor;

    @GetMapping("/sms")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateCodeProcessor.create(new ServletWebRequest(request, response));
    }
}
