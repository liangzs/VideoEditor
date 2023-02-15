package com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantPath;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Vector;

/**
 * .dat,idx滤镜处理抽象类
 */
public class MagicDatObstractFilter extends GPUImageFilter {

    private ByteBuffer mDataBuffer;
    private Vector<BitmapFileDescription> bitmapFileDescriptions;
    public int textureSize;
    public int[] inputTextureHandles;
    public int[] inputTextureUniformLocations;
    private boolean isSaveBitmap = false;

    public MagicDatObstractFilter(MagicFilterType magicFilterType, String fragmentShader, String filterResourceIndexPath,
                                  String filterResourceBinaryPackPath) {
        super(magicFilterType, NO_FILTER_VERTEX_SHADER, fragmentShader);
        readData(filterResourceIndexPath, filterResourceBinaryPackPath);
    }

    public void onDestroy() {
        super.onDestroy();
        if (inputTextureHandles != null) {
            GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
            for (int i = 0; i < inputTextureHandles.length; i++)
                inputTextureHandles[i] = -1;
        }
    }

    protected void onDrawArraysAfter() {
        for (int i = 0; i < inputTextureHandles.length
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre() {
        for (int i = 0; i < textureSize
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
            GLES20.glUniform1i(inputTextureUniformLocations[i], (i + 3));
        }
    }

    public void onInit() {
        super.onInit();
        inputTextureHandles = new int[textureSize];
        inputTextureUniformLocations = new int[textureSize];
        for (int i = 0; i < textureSize; i++)
            inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture" + (2 + i));

        //初始化纹理
        for (int i = 0; i < textureSize; i++) {
            BitmapFileDescription bitmapFileDescription = bitmapFileDescriptions.get(i);
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    mDataBuffer.array(),
                    mDataBuffer.arrayOffset() + bitmapFileDescription.startPos,
                    bitmapFileDescription.endPos);
            saveBitmapAsPng(bitmap, bitmapFileDescription.name);
            inputTextureHandles[i] = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE);
        }
    }

    private void saveBitmapAsPng(Bitmap bitmap, String name) {
        if (isSaveBitmap) {
            File outputFile = new File(ConstantPath.APPPATH,
                    "/afilter/DumpedData/" + name);
            if (!outputFile.getParentFile().exists())
                outputFile.getParentFile().mkdirs();
            BitmapUtil.savePNGBitmap(bitmap,
                    outputFile.getAbsolutePath()
            );
        }
    }


    private void readData(String filterResourceIndexPath,
                          String filterResourceBinaryPackPath) {
        bitmapFileDescriptions = new Vector<>();
        String fileName = "tempFile." + System.currentTimeMillis();
        FileUtils.copyFileFromAssets(MediaSdk.getInstance().getContext(),
                MediaSdk.getInstance().getContext().getCacheDir().getAbsolutePath(),
                fileName,
                filterResourceBinaryPackPath);
        File dataFile = new File(MediaSdk.getInstance().getContext().getCacheDir().getAbsolutePath(), fileName);
        if (!dataFile.exists()) return;

        String fileIndexContent = FileUtils.readAssetsTextFile(MediaSdk.getInstance().getContext(), filterResourceIndexPath);
        String[] bitmapFileDescriptionStr = fileIndexContent.split(";");
        for (int i = 0; i < bitmapFileDescriptionStr.length; i++) {
            String[] bitmapFileDescriptionStrSplit = bitmapFileDescriptionStr[i].split(":");
            if (bitmapFileDescriptionStrSplit.length == 3) {
                int startPos = Integer.parseInt(bitmapFileDescriptionStrSplit[1]);
                int endPos = Integer.parseInt(bitmapFileDescriptionStrSplit[2]);
                bitmapFileDescriptions.add(new BitmapFileDescription(bitmapFileDescriptionStrSplit[0], startPos, endPos));
            }
        }
        mDataBuffer = ByteBuffer.allocateDirect((int) dataFile.length());
        byte[] buf = new byte[512];
        try {
            int len;
            FileInputStream localFileInputStream = new FileInputStream(dataFile);
            while ((len = localFileInputStream.read(buf)) != -1) {
                mDataBuffer.put(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        textureSize = bitmapFileDescriptions.size();
        dataFile.delete();
    }

    private class BitmapFileDescription {
        String name;
        int startPos;
        int endPos;

        public BitmapFileDescription(String name, int startPos, int endPos) {
            this.name = name;
            this.startPos = startPos;
            this.endPos = endPos;
        }

        public String toString() {
            return "name: " + name + " startPos: " + startPos + " endPos: " + endPos;
        }
    }


}
