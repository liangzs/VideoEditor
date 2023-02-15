package com.ijoysoft.mediasdk.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ThemeTest {
    public static void main(String[] args) {
        String filePath = ("E:\\workSpace\\主题模板\\20230214\\yhz-Video maker视频模板20220113\\yhz_食物\\yhz_2食物\\food6\\food6");
        //File file = new File("hehe.txt");
        String contents = new String("ijoysoft");
        try {
            File file = new File(filePath);
            for (File f : file.listFiles()) {
                addContainsToFile(f, 0, contents);
            }
        } catch (
                IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void addContainsToFile(File file, int position, String contents) throws IOException {
        //判断文件是否存在
        if (!(file.exists() && file.isFile())) {
            System.out.println("文件不存在  ~ ");
            return;
        }
        //判断position是否合法
        if ((position < 0) || (position > file.length())) {
            System.out.println("position不合法 ~ ");
            return;
        }

        //2、创建临时文件
        File tempFile = File.createTempFile("sss", ".temp", new File("d:/"));
        //File tempFile = new File("d:/wwwww.txt");
        //3、用文件输入流、文件输出流对文件进行操作
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        //在退出JVM退出时自动删除
        tempFile.deleteOnExit();
        //4、创建RandomAccessFile流
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        //文件指定位置到 position
        rw.seek(position);

        int tmp;
        //5、将position位置后的内容写入临时文件
        while ((tmp = rw.read()) != -1) {
            outputStream.write(tmp);
        }
        //6、将追加内容 contents 写入 position 位置
        rw.seek(position);
        rw.write(contents.getBytes());

        //7、将临时文件写回文件，并将创建的流关闭
        while ((tmp = inputStream.read()) != -1) {
            rw.write(tmp);
        }
        rw.close();
        outputStream.close();
        inputStream.close();
    }
}
