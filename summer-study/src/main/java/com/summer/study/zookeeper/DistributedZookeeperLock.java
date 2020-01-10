package com.summer.study.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: create by Summer.Xiong
 * @version: v1.0
 * @description:
 *      分布式锁实现(基于Zookeeper):
 * @date: 2020/1/9 16:41
 */
@Slf4j
public class DistributedZookeeperLock implements Lock, Watcher {

    private ZooKeeper zk = null;
    private String ROOT_LOCK = "/locks";//定义锁的根节点
    private String WAIT_LOCK;//等待前一个锁
    private String CURRENT_LOCK;//当前锁
    private static final byte[] ZK_DATA = "0".getBytes();
    private CountDownLatch countDownLatch;

    public DistributedZookeeperLock() {
        try {
            //其中this是因为当前对象实现了Watcher接口,会处理Zookeeper的Watch事件
            zk = new ZooKeeper("192.168.160.191:2181", 4000, this);
            //判断当前根节点是否存在
            Stat stat = zk.exists(ROOT_LOCK, false);
            if(null == stat){
                zk.create(ROOT_LOCK, ZK_DATA, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lock() {
        if(this.tryLock()){
            log.debug("{} -> 获取锁[{}]成功", Thread.currentThread().getName(), CURRENT_LOCK);
            return;
        }
        log.debug("{} -> 获取锁[{}]失败", Thread.currentThread().getName(), CURRENT_LOCK);
        waitForLock(WAIT_LOCK); //等待锁释放
    }

    /**
     * 监听自己节点的上一个节点
     * @param prevlock
     */
    private boolean waitForLock(String prevlock) {
        try {
            Stat stat = zk.exists(prevlock, true);
            if(null != stat){
                log.debug("{} -> 等待锁[{}]释放，然后锁定[{}]", Thread.currentThread().getName(), WAIT_LOCK, CURRENT_LOCK);
                countDownLatch = new CountDownLatch(1);
                countDownLatch.await();
                log.debug("{} -> 锁[{}]已释放，[{}]获取锁", Thread.currentThread().getName(), WAIT_LOCK, CURRENT_LOCK);
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            //创建临时有序节点
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/", ZK_DATA, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.debug("{} -> [{}]尝试竞争锁", Thread.currentThread().getName(), CURRENT_LOCK);

            List<String> childrens = zk.getChildren(ROOT_LOCK, false);
            SortedSet<String> sortedSet = new TreeSet<>();
            for (String children: childrens){
                sortedSet.add(ROOT_LOCK + "/" + children);
            }

            String firstNode = sortedSet.first();
            //会返回CURRENT_LOCK更小的节点的集合
            SortedSet<String> lessThenMe = sortedSet.headSet(CURRENT_LOCK);
            if(CURRENT_LOCK.equals(firstNode)){
                log.debug("{} -> [{}]已获取锁", Thread.currentThread().getName(), CURRENT_LOCK);
                return true;
            }

            if(!lessThenMe.isEmpty()){
                WAIT_LOCK = lessThenMe.last();//获取比当前节点更小的最后一个节点, 设置给WAIT_LOCK
                log.debug("{} -> [{}]等待锁[{}]释放", Thread.currentThread().getName(), CURRENT_LOCK, WAIT_LOCK);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        log.debug("{} -> [{}]释放锁资源", Thread.currentThread().getName(), CURRENT_LOCK);
        try {
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * Zookeeper Watch
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if(null !=  countDownLatch){
            log.debug("Zookeeper监听 -> [{}]释放锁资源", WAIT_LOCK);
            countDownLatch.countDown();
        }
    }
}
