package com.lee9213.thread;

import com.lee9213.thread.util.SleepUtils;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/12 16:09
 */
public class ThreadState {

    public static void main(String[] args) {
        // new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        // new Thread(new Waiting(), "WaitingThread").start();
        //
        // new Thread(new Blocked(), "BlockedThread-1").start();
        // new Thread(new Blocked(), "BlockedThread-2").start();

        Thread daemon = new Thread(new DaemonThread(), "DaemonThread");
        daemon.setDaemon(true);
        daemon.start();

    }

    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
                System.out.println("TimeWaiting");
            }
        }
    }

    static class Waiting implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                        System.out.println("Waiting");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    static class Blocked implements Runnable {
        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(10);
                    System.out.println("Blocked");
                }
            }
        }
    }

    static class DaemonThread implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(10);
            } finally {
                System.out.println("DaemonThread finally run");
            }
        }
    }
}
