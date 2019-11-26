package com.summer.study.singleton;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *              单例模式: 懒汉式(双层校验锁)
 * @date:2019/11/26
 */
public class Singleton2 {

    private Singleton2(){

    }

    private static volatile Singleton2 singleton = null;

    public static Singleton2 getInstance(){
        if(null == singleton){
            synchronized (Singleton2.class){
                if(null == singleton){
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }
}
