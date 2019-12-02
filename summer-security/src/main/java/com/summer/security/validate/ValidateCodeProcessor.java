package com.summer.security.validate;

import com.summer.security.validate.generator.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.validate.code
 * @date:2019/12/2
 */
public interface ValidateCodeProcessor {

    /**
     * 创建校验码
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;
}
