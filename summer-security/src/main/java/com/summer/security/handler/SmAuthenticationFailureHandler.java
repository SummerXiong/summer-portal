package com.summer.security.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *      SpringSecurity登录失败处理器
 *          可以在此处记录用户登录失败的次数, 锁定登录账户, 或者指定登录失败时返回的数据格式
 * @date:2019/11/28
 */
@Component
public class SmAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //如果此处配置成参数, 则可动态设置登录失败后的操作
        if(true) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(exception));
            return;
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
