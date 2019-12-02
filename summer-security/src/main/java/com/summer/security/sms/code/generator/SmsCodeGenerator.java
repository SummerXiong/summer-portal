package com.summer.security.sms.code.generator;

import com.summer.security.validate.generator.ValidateCodeGenerator;
import com.summer.security.validate.model.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          短信验证码生成器
 * @date:2019/12/2
 */
@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(4);
        return new ValidateCode(code, 60);
    }

}
