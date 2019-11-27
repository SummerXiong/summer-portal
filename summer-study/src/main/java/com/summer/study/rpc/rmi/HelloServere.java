package com.summer.study.rpc.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *          1、创建远程接口,并且继承java.rmi.Remote接口
 *          2、实现远程接口,并且继承UnicastRemoteObject
 *          3、创建服务器程序: createRegistry方法注册远程对象
 * @date:2019/11/26
 */
public class HelloServere {

    public static void main(String[] args) {
        try {
            ISayHello sayHello = new SayHelloImpl();
            LocateRegistry.createRegistry(9999);
            Naming.bind("rmi://localhost:9999/sayHello", sayHello);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
