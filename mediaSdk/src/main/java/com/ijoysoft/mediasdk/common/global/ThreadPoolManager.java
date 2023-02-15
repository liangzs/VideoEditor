package com.ijoysoft.mediasdk.common.global;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static ThreadPool mThreadPool;

    public static ThreadPool getThreadPool() {
        int N = Runtime.getRuntime().availableProcessors();
        if (mThreadPool == null) {
            synchronized (ThreadPoolManager.class) {
                if (mThreadPool == null) {
                    int cpuCount = Runtime.getRuntime().availableProcessors();
                    int threadCount = cpuCount + 1;
                    mThreadPool = new ThreadPool((int) (N), threadCount * 3, 1L);
                }
            }
        }

        return mThreadPool;
    }

    public static class ThreadPool {
        private int corePoolSize;// 核心线程数
        private int maximumPoolSize;// 最大线程数
        private long keepAliveTime;// 休息时间

        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
            }
            if (!executor.isShutdown()) {
                executor.execute(r);
            }
        }


        public boolean isExecute() {
            return executor == null ? true : false;
        }

        public void cancel(Runnable r) {
            if (executor != null) {
                executor.getQueue().remove(r);
            }
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

        /**
         * 立马清理线程池，针对快速选中很多视频文件，然后回到主页，选择照片
         */
        public void shutDownNow() {
            if (executor == null) {
                return;
            }
            try {
                executor.shutdownNow(); // Disable new tasks from being submitted
                // 等待 60 s
                if (!executor.awaitTermination(14, TimeUnit.SECONDS)) {
                    // 调用 shutdownNow 取消正在执行的任务
                    executor.shutdownNow();
                    // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                    if (!executor.awaitTermination(14, TimeUnit.SECONDS))
                        System.err.println("线程池任务未正常执行结束");
                }
            } catch (Exception ie) {
                // 重新调用 shutdownNow
                ie.printStackTrace();
            }
            release();
        }
    }
}
