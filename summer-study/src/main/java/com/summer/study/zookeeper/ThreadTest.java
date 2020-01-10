package com.summer.study.zookeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 * @date: 2020/1/10 10:39
 */
public class ThreadTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    DistributedZookeeperLock distributedZookeeperLock = new DistributedZookeeperLock();
                    boolean result = distributedZookeeperLock.tryLock();
                    if(result){
                        distributedZookeeperLock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Thread-" + i).start();
            countDownLatch.countDown();
        }
    }
}
