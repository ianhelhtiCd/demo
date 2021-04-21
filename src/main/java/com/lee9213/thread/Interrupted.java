package com.lee9213.thread;

import com.lee9213.thread.util.SleepUtils;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/13 14:56
 */
public class Interrupted {
    public static void main(String[] args) {
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);

        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();

        SleepUtils.second(5);
        System.out.println("1SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("2BusyThread interrupted is " + busyThread.isInterrupted());
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("3SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("4BusyThread interrupted is " + busyThread.isInterrupted());

        SleepUtils.second(2);
    }


    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {

            }
        }
    }
}
