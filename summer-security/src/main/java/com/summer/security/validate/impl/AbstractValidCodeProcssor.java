package com.summer.security.validate.impl;

import com.summer.security.validate.ValidateCodeProcessor;
import com.summer.security.validate.generator.ValidateCodeGenerator;
import com.summer.security.validate.model.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          模板模式处理验证码发送流程
 *          1、图形验证码
 *          2、短信验证码
 * @date:2019/12/2
 */
public abstract class AbstractValidCodeProcssor implements ValidateCodeProcessor {

    private ValidateCodeGenerator validateCodeGenerator;

    @Override
    public void create(ServletWebRequest request) throws Exception {

        this.validateCodeGenerator = this.getValidateCodeGenerator();

        //创建验证码
        ValidateCode validateCode = generate(request);
        //保存验证码
        save(request, validateCode);
        //发送验证码
        send(request, validateCode);
    }

    protected abstract void save(ServletWebRequest request, ValidateCode validateCode);

    protected abstract ValidateCodeGenerator getValidateCodeGenerator();

    protected abstract void send(ServletWebRequest request, ValidateCode validateCode);

    private ValidateCode generate(ServletWebRequest request){
        return validateCodeGenerator.generate(request);
    }

}
