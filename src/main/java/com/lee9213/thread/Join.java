package com.lee9213.thread;

import com.lee9213.thread.util.SleepUtils;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/14 15:44
 */
public class Join {

    public static void main(String[] args) {
        Thread previous = Thread.currentThread();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();

            previous = thread;
        }

        SleepUtils.second(5);
        System.out.println(Thread.currentThread().getName() + " terminate.");
    }
    static class Domino implements Runnable {

        private Thread previous;

        public Domino(Thread previous) {
            this.previous = previous;
        }

        @Override
        public void run() {
            try {
                previous.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminate.");
        }
    }
}
