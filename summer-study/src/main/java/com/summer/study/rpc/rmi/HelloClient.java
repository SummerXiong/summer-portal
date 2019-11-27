package com.summer.study.rpc.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description: com.summer.study.rpc.rmi
 * @date:2019/11/26
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
            ISayHello hello = (ISayHello) Naming.lookup("rmi://localhost:9999/sayHello");
            System.out.println(hello.sayHello("张三"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
