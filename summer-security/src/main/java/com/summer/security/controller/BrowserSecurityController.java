package com.summer.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.security.controller
 * @date:2019/11/28
 */
@RestController
public class BrowserSecurityController {

    /**
     * 可以通过此类把刚才请求取出
     */
    protected RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * SpringSecurity跳转地址的工具类
     */
    protected RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 当需要身份认证时,需要跳转到这里处理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String requireAuthenication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //获取引发跳转的请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(null != savedRequest){
            String target = savedRequest.getRedirectUrl();
            //判断请求的地址是否是一个html还是其他, 来判断返回的是一个页面还是一个json, 是页面则跳转登录页
            if(StringUtils.endsWithIgnoreCase(target, ".html")){
                redirectStrategy.sendRedirect(request, response, "/xxx/xxx");// /xxx/xxx登录页地址
            }
        }
        //或者返回一个JSON信息给前台

        return "请跳转登录!";
    }
}
