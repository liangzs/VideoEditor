package com.qiusuo.videoeditor.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
    /**
     * 关闭流
     *
     * @param stream
     */
    public static final void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件 返回字符串
     *
     * @param src
     * @return
     */
    public static String readFile(File src) {
        try {
            return readStream(new FileInputStream(src));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(File src, String charset) {
        try {
            return readStream(new FileInputStream(src), charset);
        } catch (FileNotFoundException var3) {
            return null;
        }
    }

    public static String readStream(InputStream in, String charset) {
        BufferedReader br = null;
        String ret = null;

        try {
            Charset encode;
            if (charset == null) {
                encode = Charset.defaultCharset();
            } else {
                encode = Charset.forName(charset);
            }

            br = new BufferedReader(new InputStreamReader(in, encode));
            String line = null;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            ret = sb.toString();
        } catch (IOException var10) {
        } finally {
            close((Closeable) br);
        }

        return ret;
    }

    /**
     * 读取输入流 返回字符串
     *
     * @param in
     * @return
     */
    public static String readStream(InputStream in) {
        BufferedReader br = null;
        String ret = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ret = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(br);
        }
        return ret;
    }

    /**
     * 向文件中写入字符串
     *
     * @param src
     * @return
     */
    public static boolean writeString(String src, File dst) {
        return writeString(src, dst, false);
    }

    /**
     * 向文件中写入字符串
     *
     * @param src
     * @return
     */
    public static boolean writeString(String src, File dst, boolean append) {
        byte[] arr = src.getBytes();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new ByteArrayInputStream(arr);
            out = new FileOutputStream(dst, append);
            writeStream(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }
        return false;
    }

    /**
     * 向输出流写入输入流
     *
     * @param in
     * @param out
     * @return
     */
    public static void writeStream(InputStream in, OutputStream out)
            throws IOException {
        if (in == null || out == null) {
            return;
        }
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);

        }
        out.flush();
    }

    public static boolean writeFile(File src, File dst) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
            writeStream(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }
        return false;
    }


    /**
     * 返回分离出MP3文件中的数据帧的文件路径
     */
    public static String fenLiData(String path) {
        File file = new File(path);// 原文件
        File file1 = new File(path + "01");// 分离ID3V2后的文件,这是个中间文件，最后要被删除
        File file2 = new File(path + "001");// 分离id3v1后的文件
        RandomAccessFile rf = null;// 随机读取文件
        FileOutputStream fos = null;
        byte ID3[] = new byte[3];
        try {
            rf = new RandomAccessFile(file, "rw");
            fos = new FileOutputStream(file1);
            rf.read(ID3);
            String ID3str = new String(ID3);
            // 分离ID3v2
            if (ID3str.equals("ID3")) {
                rf.seek(6);
                byte[] ID3size = new byte[4];
                rf.read(ID3size);
                int size1 = (ID3size[0] & 0x7f) << 21;
                int size2 = (ID3size[1] & 0x7f) << 14;
                int size3 = (ID3size[2] & 0x7f) << 7;
                int size4 = (ID3size[3] & 0x7f);
                int size = size1 + size2 + size3 + size4 + 10;
                rf.seek(size);
                int lens = 0;
                byte[] bs = new byte[1024 * 4];
                while ((lens = rf.read(bs)) != -1) {
                    fos.write(bs, 0, lens);
                }
                fos.close();
                rf.close();
            } else {// 否则完全复制文件
                int lens = 0;
                rf.seek(0);
                byte[] bs = new byte[1024 * 4];
                while ((lens = rf.read(bs)) != -1) {
                    fos.write(bs, 0, lens);
                }
                fos.close();
                rf.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file1, "rw");
            byte TAG[] = new byte[3];
            raf.seek(raf.length() - 128);
            raf.read(TAG);
            String tagstr = new String(TAG);
            if (tagstr.equals("TAG")) {
                FileOutputStream fs = new FileOutputStream(file2);
                raf.seek(0);
                byte[] bs = new byte[(int) (raf.length() - 128)];
                raf.read(bs);
                fs.write(bs);
                raf.close();
                fs.close();
            } else {// 否则完全复制内容至file2
                FileOutputStream fs = new FileOutputStream(file2);
                raf.seek(0);
                byte[] bs = new byte[1024 * 4];
                int len = 0;
                while ((len = raf.read(bs)) != -1) {
                    fs.write(bs, 0, len);
                }
                raf.close();
                fs.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file1.exists())// 删除中间文件
        {
            file1.delete();

        }
        return file2.getAbsolutePath();
    }

    /**
     * 分离出数据帧每一帧的大小并存在list数组里面
     * 失败则返回空
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static List<Integer> initMP3Frame(String path) {
        File file = new File(path);
        List<Integer> list = new ArrayList<>();
		/*	int framSize=0;
			RandomAccessFile rad = new RandomAccessFile(file, "rw");
			byte[] head = new byte[4];
			rad.seek(framSize);
			rad.read(head);
			int bitRate = getBitRate((head[2] >> 4) & 0x0f) * 1000;
			int sampleRate = getsampleRate((head[2] >> 2) & 0x03);
			int paing = (head[2] >> 1) & 0x01;
			int len = 144 * bitRate / sampleRate + paing;
			for(int i=0,lens=(int)(file.length())/len;i<lens;i++){
				list.add(len);// 将数据帧的长度添加进来
			}*/
        int framSize = 0;
        RandomAccessFile rad = null;
        try {
            rad = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (framSize < file.length()) {
            byte[] head = new byte[4];
            try {
                rad.seek(framSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rad.read(head);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int bitRate = getBitRate((head[2] >> 4) & 0x0f) * 1000;
            int sampleRate = getsampleRate((head[2] >> 2) & 0x03);
            int paing = (head[2] >> 1) & 0x01;
            if (bitRate == 0 || sampleRate == 0) return null;
            int len = 144 * bitRate / sampleRate + paing;
            list.add(len);// 将数据帧的长度添加进来
            framSize += len;
        }
        return list;
    }

    private static int getBitRate(int i) {
        int a[] = {0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224,
                256, 320, 0};
        return a[i];
    }

    private static int getsampleRate(int i) {
        int a[] = {44100, 48000, 32000, 0};
        return a[i];
    }


    /**
     * 返回切割后的MP3文件的路径 返回null则切割失败 开始时间和结束时间的整数部分都是秒，以秒为单位
     *
     * @param list
     * @param startTime
     * @param stopTime
     * @return
     * @throws IOException
     */
    public static String CutingMp3(String src, String des, String name,
                                   List<Integer> list, long startTime, long stopTime) {
        File file = new File(src);

        File desfile = new File(des);
        if (!desfile.exists()) {
            desfile.mkdirs();
        }
        int start = (int) (startTime / 0.026);
        int stop = (int) (stopTime / 0.026);
        if ((start > stop) || (start < 0) || (stop < 0) || (stop > list.size())) {
            return null;
        } else {
            long seekStart = 0;// 开始剪切的字节的位置
            for (int i = 0; i < start; i++) {
                seekStart += list.get(i);
            }
            long seekStop = 0;// 结束剪切的的字节的位置
            for (int i = 0; i < stop; i++) {
                seekStop += list.get(i);
            }
            RandomAccessFile raf = null;
            FileOutputStream out = null;
            File file1 = new File(des + "/" + name);
            if (!file1.exists()) {
                try {
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                raf = new RandomAccessFile(file, "rw");
                raf.seek(seekStart);
                out = new FileOutputStream(file1);
                byte[] bs = new byte[(int) (seekStop - seekStart)];
                raf.read(bs);
                out.write(bs);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                    if (out != null) {
                        out.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return file1.getAbsolutePath();
        }

    }


}
