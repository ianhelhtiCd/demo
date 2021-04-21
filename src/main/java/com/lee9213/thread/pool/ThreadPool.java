package com.lee9213.thread.pool;

/**
 * @author libo
 * @version 1.0
 * @date 2017/7/20 14:38
 */
public interface ThreadPool<Job extends Runnable> {

    void execute(Job job);

    void shutdown();

    void addWorkers(int num);

    void removeWorker(int num);

    int getJobSize();
}
