package com.summer.study.singleton;

import lombok.Data;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *      单例模式: 饿汉式
 * @date:2019/11/21
 */
@Data
public class Singleton1 {

    private Singleton1() {
    }

    public static final Singleton1 getInstance(){
        return LazyHolder.singleton;
    }

    private static class LazyHolder{
        private static final Singleton1 singleton = new Singleton1();
    }

}
