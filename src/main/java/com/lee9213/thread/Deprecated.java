package com.lee9213.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lee9213.thread.util.SleepUtils;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/13 15:43
 */
public class Deprecated {

    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(), "PrintThread");
        printThread.setDaemon(true);
        printThread.start();

        SleepUtils.second(3);
        printThread.suspend();
        System.out.println("main suspend PrintThread at " + format.format(new Date()));

        SleepUtils.second(3);
        printThread.resume();
        System.out.println("main resume PrintThread at " + format.format(new Date()));

        SleepUtils.second(3);
        printThread.stop();
        System.out.println("main stop PrintThread at " + format.format(new Date()));

        SleepUtils.second(3);
    }


    static class Runner implements Runnable {
        @Override
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(Thread.currentThread().getName() + "Run at " + format.format(new Date()));
                SleepUtils.second(1);
            }
        }
    }
}
