package com.ijoysoft.mediasdk.common.global;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMaxThread {

    private ExecutorService executor;


    private ThreadPoolMaxThread() {
        executor = Executors.newCachedThreadPool();
    }

    private static class SingleTonHoler {
        private static ThreadPoolMaxThread INSTANCE = new ThreadPoolMaxThread();
    }

    public static ThreadPoolMaxThread getInstance() {
        return SingleTonHoler.INSTANCE;
    }


    public void execute(Runnable runnable) {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        if (!executor.isShutdown()) {
            executor.execute(runnable);
        }
    }


    public boolean isExecute() {
        return executor == null ? true : false;
    }


    public void shutDown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public boolean isTerminted() {
        if (executor != null) {
            return executor.isTerminated();
        }
        return false;
    }

    public boolean awaitTermination(long time) {
        if (executor == null) {
            return false;
        }
        try {
            return executor.awaitTermination(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void release() {
        executor = null;
    }
}
