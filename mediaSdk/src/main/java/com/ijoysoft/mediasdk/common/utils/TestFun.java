package com.ijoysoft.mediasdk.common.utils;

import com.ijoysoft.mediasdk.module.ffmpeg.VideoEditManager;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestFun {
    private static final Object lock = new Object();

    public Object getLock() {
        return lock;
    }

    public static class Test1 extends TestFun {
    }

    public static class Test2 extends TestFun {
    }

    @Test
    public void calc() {
        String BASE = "E:\\workSpace\\测试环境\\videomaker";
//<item index="13" name="travel14" path="/theme/travel/travel14.zip" size="307747" musicPath="/music/happy/AtTheFair.mp3"/>
        List<String> theme = Arrays.asList(
                "\\theme\\daily\\daily0.zip",
                "\\theme\\daily\\daily1.zip",
                "\\theme\\daily\\daily2.zip",
                "\\theme\\daily\\daily3.zip",
                "\\theme\\food\\food0.zip",
                "\\theme\\travel\\travel14.zip"
        );
        List<String> music = Arrays.asList(
                "\\music\\travel\\Come-Back .mp3",
                "\\music\\travel\\Summertime.mp3",
                "\\music\\travel\\As_I_Figure.mp3",
                "\\music\\travel\\ready_for_summer.mp3",
                "\\music\\travel\\summer-technology.mp3",
                "\\music\\travel\\Blue_Sky_Over_Hawaii.mp3"
        );
        File file1;
        File file2;
        for (int i = 0; i < theme.size(); i++) {
            file1 = new File(BASE + theme.get(i));
            file2 = new File(BASE + music.get(i));
            System.out.println(file1.length() + file2.length());
        }
    }


    @org.junit.Test
    public void relock() {
        Lock dynamicMipmapsLoadLock = new ReentrantLock();
        System.out.println("111111111111111");
        if (dynamicMipmapsLoadLock.tryLock()) {
            try {
                //缓存初始化
                Thread.sleep(100);
                System.out.println("222222222222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                dynamicMipmapsLoadLock.unlock();
                System.out.println(Thread.currentThread().getName());
            }
        }
        System.out.println("333333333333333333");
    }


    @Test
    public void composeAudio() {

        System.out.println(2 % 1);
        System.out.println(3 % 2);

//        String outPath = "/storage/emulated/0/Android/data/media.moviestudio.video.maker/files/ijoysoft" +
//                "/e6855758-d557-49c6-81aa-57602dff6ae0/murge/573dfbc970b84e1a9f778f53e186362e_cut_again_preview.mp3";
//
//        String offsetPath = "/storage/emulated/0/Android/data/media.moviestudio.video.maker/files" +
//                "/ijoysoft/e6855758-d557-49c6-81aa-57602dff6ae0/20230116_145300572audio_cut_preview";
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            list.add(offsetPath);
//        }
//        List<String[]> result = VideoEditManager.composeMultiAudio(outPath, null, false, list.toArray(new String[list.size()]));
//
//        for (String[] s : result) {
//            System.out.println(Arrays.toString(s));
//        }
    }

}

