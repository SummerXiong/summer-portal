package com.summer.security.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          SpringSecurity登录成功处理类
 * @date:2019/11/28
 */
@Component
public class SmAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        //如果此处配置成参数, 则可动态设置登录成功后的操作
        if(true){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(authentication));
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
