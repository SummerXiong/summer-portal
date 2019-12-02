package com.summer.security.sms.code.processor;

import com.summer.security.sms.code.generator.SmsCodeGenerator;
import com.summer.security.sms.filter.SmsCodeAuthenticationFilter;
import com.summer.security.validate.generator.ValidateCodeGenerator;
import com.summer.security.validate.impl.AbstractValidCodeProcssor;
import com.summer.security.validate.model.ValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.sms.code.processor
 * @date:2019/12/2
 */
@Component
public class SmsValidateCodeProcessor extends AbstractValidCodeProcssor {

    private final static Logger logger = LoggerFactory.getLogger(SmsValidateCodeProcessor.class);

    @Autowired
    protected SmsCodeGenerator smsCodeGenerator;

    @Override
    protected ValidateCodeGenerator getValidateCodeGenerator() {
        return smsCodeGenerator;
    }

    /**
     * 将短信验证码保存到redis or session中, 方便取出对比
     * @param request
     * @param validateCode
     */
    @Override
    protected void save(ServletWebRequest request, ValidateCode validateCode) {

    }

    /**
     * 可以从request对象中获取是哪个手机号,请求验证码
     * @param request
     * @param validateCode
     */
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) {
        String mobile = request.getRequest().getParameter(SmsCodeAuthenticationFilter.SPRING_SECURITY_FORM_MOBILE_KEY);
        logger.info("向手机{}发送短信验证码: {}", mobile, validateCode.getCode());
    }
}
