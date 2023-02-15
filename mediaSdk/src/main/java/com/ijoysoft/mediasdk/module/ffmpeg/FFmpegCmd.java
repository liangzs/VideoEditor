package com.ijoysoft.mediasdk.module.ffmpeg;

import android.os.Handler;
import android.os.Looper;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import xcrash.XCrash;

/**
 * 唉，因为ffmpegcmd下只支持单个执行环境，意思就是说并发情况会出现报错，
 * 为了避免这种情况，重新进行改造，支持流动式订阅方式
 * <p>
 * 最后考虑引入rxjava，主要是因为后续的音频处理，很需要这样的链式处理
 * 不过引入后，threadPool变成了退休状态了
 */
public class FFmpegCmd {
    static {
//        System.loadLibrary("x264");
//        System.loadLibrary("mp3lame");
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ijoysoft-ffmpeg");
    }

    private static final String TAG = "FFmpegCmd";
    private static ExecutorService executor;
    private static CallCmdListener cmdListener;
    private static int listPassProgress;
    private static float listProgressPercent;
    private static int totalTime;
    private static int progress;
    private boolean isCancel;
    private static String commandString;

    private static ErrorCallback errorCallback;

    private static class InstanceHolder {
        private static FFmpegCmd INSTANCE = new FFmpegCmd();
    }

    public static FFmpegCmd getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    public FFmpegCmd() {
        executor = Executors.newSingleThreadExecutor();
    }

    private void checkShutDown() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
    }

    // 开子线程调用native方法进行音视频处理
    public void execute(final String[] commands, final CallCmdListener onHandleListener) {
        checkShutDown();
        executor.submit(() -> {
            long start = System.currentTimeMillis();
            LogUtils.i(TAG, Arrays.toString(commands) + ",execute-start:");
            cmdListener = onHandleListener;
            if (onHandleListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onHandleListener.onStart();
                    }
                });
            }
            // 调用ffmpeg进行处理
            commandString = Arrays.toString(commands);
            final int result = handle(commands, commandString);
            final int time = (int) (System.currentTimeMillis() - start);
            LogUtils.i(TAG, Arrays.toString(commands) + ",time:" + time);
            if (onHandleListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onHandleListener.onNext();
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onHandleListener.onStop(time);
                    }
                });
                // 这个在内部executor里面去走，已避免下一步的ffmpeg指令需要的数据没有完成
                onHandleListener.onInnerFinish();
            }
            errorCallback = null;
        });

    }

    /**
     * 采用同步就好了，省了这么多的麻烦事
     *
     * @param list
     */
    public void executeListProgress(final List<String[]> list, final List<Integer> helpers,
                                    final CallCmdListener listener) {
        checkShutDown();
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        isCancel = false;
        listPassProgress = 0;
        cmdListener = listener;
        executor.submit(new Runnable() {
            @Override
            public void run() {
                listProgressPercent = 1.0f / list.size();
                int average = 100 / list.size();
                LogUtils.i(TAG, "average:" + average + ",listProgressPercent:" + listProgressPercent);
                long start = System.currentTimeMillis();
                for (int i = 0; i < list.size(); i++) {
                    if (isCancel) {
                        listPassProgress = 0;
                        listProgressPercent = 1.0f;
                        LogUtils.i(TAG, "had cancel  executeList--time:" + (System.currentTimeMillis() - start));
                        break;
                    }
                    totalTime = helpers.get(i);
                    LogUtils.i(TAG, "current_totalTime:" + totalTime);
                    commandString = Arrays.toString(list.get(i));
                    handleByProgress(list.get(i), commandString);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listPassProgress += average;
                    if (i == list.size() - 1 && listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onStop(0);
                                listener.onNext();
                            }
                        });
                        listener.onInnerFinish();
                        errorCallback = null;
                    }
                }
                listPassProgress = 0;
                listProgressPercent = 1.0f;
                LogUtils.i(TAG, "executeList--time:" + (System.currentTimeMillis() - start));
            }
        });
    }

    /**
     * 采用同步就好了，省了这么多的麻烦事
     *
     * @param list
     */
    public void executeList(final List<String[]> list, final CallCmdListener listener) {
        isCancel = false;
        checkShutDown();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (int i = 0; i < list.size(); i++) {
                    if (isCancel) {
                        LogUtils.i(TAG, "had cancel  executeList--time:" + (System.currentTimeMillis() - start));
                        list.clear();
                        break;
                    }

                    commandString = Arrays.toString(list.get(i));
                    handle(list.get(i), commandString);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isCancel) {
                        LogUtils.i(TAG, "had cancel  executeList--time1:" + (System.currentTimeMillis() - start));
                        break;
                    }
                    if (listener != null) {
                        final int finalI = i + 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onProgress(finalI * 100 / list.size());
                            }
                        });

                    }
                    if (i == list.size() - 1 && listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onStop(0);
                                listener.onNext();
                            }
                        });
                        listener.onInnerFinish();
                        errorCallback = null;
                    }
                }
                LogUtils.i(TAG, "executeList--time:" + (System.currentTimeMillis() - start));
            }
        });
    }

    // 开子线程调用native方法进行音视频处理
    public void executeProgress(final String[] commands, final CallCmdListener onHandleListener, final int time) {
        checkShutDown();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                LogUtils.i(TAG, commandString + ",executeProgress-start");
                listProgressPercent = 1.0f;
                long start = System.currentTimeMillis();
                cmdListener = onHandleListener;
                totalTime = Integer.valueOf(time);
                if (onHandleListener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onHandleListener.onStart();
                        }
                    });
                }
                commandString = Arrays.toString(commands);
                // 调用ffmpeg进行处理
                final int result = handleByProgress(commands, commandString);
                LogUtils.i(TAG, commandString + ",time:" + (System.currentTimeMillis() - start));
                if (onHandleListener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onHandleListener.onNext();
                        }
                    });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onHandleListener.onStop(result);
                        }
                    });
                    onHandleListener.onInnerFinish();
                    errorCallback = null;
                }
            }
        });
    }

    public native int handle(String[] commands, String commandStr);

    public native int handleByProgress(String[] commands, String commandStr);

    public native int cancelJni();

    public void cancel() {
        isCancel = true;
        // shutDown();//发现有的手机报错严重影响问题（58手机）
    }

    public static void progress(int second) {
        LogUtils.i(TAG, "second:" + second);
        final int secondTemp = second;
        if (cmdListener != null && second > 0 && totalTime != 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progress = (int) (listPassProgress + (secondTemp * 100000 * listProgressPercent / totalTime));
                    LogUtils.i(TAG, "second:" + secondTemp + ",listPassProgress:" + listPassProgress + ",totalTime:"
                            + totalTime + ",result;" + progress);
                    if (progress > 100) {
                        progress = 100;
                    }
                    cmdListener.onProgress(progress);
                }
            });

        }
    }

    /**
     * 这里可以测试一下，如果继续抛出bugly能不能捕获而google console不能捕
     */
    public static void error() {
        // XCrash.testNativeCrash(true);
        LogUtils.i("", "ffmpegcmd->error--------------------------");
        //errorCallback 针对子进程服务，杀到子进程
        if (errorCallback != null) {
            errorCallback.callBack();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }
        //bugly收集具体日志
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                throw new JNIException(commandString);
//                // android.os.Process.killProcess(android.os.Process.myPid());
//                // System.exit(0);
//            }
//        });
    }

    public static void setErrorCallBack(ErrorCallback callBack) {
        errorCallback = callBack;
    }

    /**
     * 处理ffmpeg中发生的异常，如果发生了异常，则新建线程进行处理，原线程清理
     */
    public void handleJniException() {
        // executor.shutdownNow();
        // executor = null;
        // executor = Executors.newSingleThreadExecutor();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (cmdListener != null) {
                    cmdListener.error(commandString.replaceAll(":", ""));
                }
                LogUtils.e(TAG, "handle-error" + commandString);
            }
        });
    }

    /**
     * 立马清理线程池，针对快速选中很多视频文件，然后回到主页，选择照片
     */
    public void shutDownNow() {
        try {
            LogUtils.e(TAG, "shutDownNow..");
            if (executor != null) {
                executor.shutdownNow(); // Disable new tasks from being submitted
            }
            // 等待 60 s
        } catch (Exception ie) {
            // 重新调用 shutdownNow
            ie.printStackTrace();
        } finally {
            executor = null;
        }
    }

    public void shutDown() {
        try {
            LogUtils.e(TAG, "shutDownNow..");
            executor.shutdown(); // Disable new tasks from being submitted
            // 等待 60 s
        } catch (Exception ie) {
            // 重新调用 shutdownNow
            ie.printStackTrace();
        } finally {
            executor = null;
        }
    }

    public interface ErrorCallback {
        void callBack();
    }

}
