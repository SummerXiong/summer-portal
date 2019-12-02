package com.summer.security.validate.generator;

import com.summer.security.validate.model.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.validate.generator
 * @date:2019/12/2
 */
public interface ValidateCodeGenerator {

    ValidateCode generate(ServletWebRequest request);
}
