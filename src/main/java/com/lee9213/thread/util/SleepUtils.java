package com.lee9213.thread.util;

import java.util.concurrent.TimeUnit;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/13 14:01
 */
public final class SleepUtils {

    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
