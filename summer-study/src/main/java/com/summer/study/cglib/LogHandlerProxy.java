package com.summer.study.cglib;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *      cglib 动态代理
 * @date:2019/11/21
 */
public class LogHandlerProxy implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object createProxy(Class clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        logger.info("before handle logger...");
        Object object = methodProxy.invokeSuper(obj, args);
        logger.info("after handle logger...");
        return object;
    }
}
