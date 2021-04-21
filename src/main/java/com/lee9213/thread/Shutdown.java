package com.lee9213.thread;

import com.lee9213.thread.util.SleepUtils;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/13 16:03
 */
public class Shutdown {

    public static void main(String[] args) {
        Runner one = new Runner();
        Thread countThread = new Thread(one,"CountThread");
        countThread.start();

        SleepUtils.second(1);

        countThread.interrupt();

        Runner two = new Runner();
        countThread = new Thread(two,"CountThread");
        countThread.start();

        SleepUtils.second(1);
        two.cancel();
    }

    private static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("count i = " + i);
        }

        public void cancel() {
            on = false;
        }
    }
}
