package com.summer.security.sms.filter;

import com.summer.security.handler.SmAuthenticationFailureHandler;
import com.summer.security.sms.exception.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *         短信的验证码校验逻辑
 * @date:2019/12/2
 */
public class SmsCodeValidateFilter extends OncePerRequestFilter implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(SmsCodeValidateFilter.class);

    private SmAuthenticationFailureHandler smAuthenticationFailureHandler;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public SmsCodeValidateFilter(SmAuthenticationFailureHandler smAuthenticationFailureHandler) {
        this.smAuthenticationFailureHandler = smAuthenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String smsCode = request.getParameter("smsCode");
        logger.debug("smsCode : {}" + smsCode);
        /**
         * 拦截是手机号登录请求,需要验证其验证码是否有效
         * 这个匹配的 /mobile 路径是与 SmsCodeValidateFilter中的AntPathRequestMatcher一致
         */
        if(pathMatcher.match("/mobile/login", request.getRequestURI())){
            try{
                //从redis or session取出短信验证码,和请求发过来的短信验证码做匹配

                /**
                 * throw new ValidateCodeException("SmsCode is null");
                 */

                /**
                 * throw new ValidateCodeException("Could not find the smsCode");
                 */

                /**
                 * throw new ValidateCodeException("SmsCode does not match");
                 */

            }catch (ValidateCodeException e){
                smAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }
}
