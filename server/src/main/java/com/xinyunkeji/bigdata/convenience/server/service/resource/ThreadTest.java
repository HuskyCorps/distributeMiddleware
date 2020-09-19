package com.xinyunkeji.bigdata.convenience.server.service.resource;

/**
 * 线程死锁
 *
 * @author Yuezejian
 * @date 2020年 09月08日 21:42:18
 */
public class ThreadTest {
    class A{}
    class B{}
    public static void main(String[] args) {
        new Thread( ()-> {
            synchronized (A.class) {
                synchronized (B.class) {
                    System.out.println("A");
                }
            }
        }).start();
        new Thread( ()-> {
            synchronized (B.class) {
                synchronized (A.class) {
                    System.out.println("B");
                }
            }
        }).start();


    }
}