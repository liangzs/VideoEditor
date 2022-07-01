package com.ijoysoft.mediasdk.module.opengl.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.text.TextUtils;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import static com.ijoysoft.mediasdk.module.opengl.filter.BackgroudFilter.ROT_0;
import static com.ijoysoft.mediasdk.module.opengl.filter.BackgroudFilter.ROT_180;
import static com.ijoysoft.mediasdk.module.opengl.filter.BackgroudFilter.ROT_270;
import static com.ijoysoft.mediasdk.module.opengl.filter.BackgroudFilter.ROT_90;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 图片源，图片的最原始纹理存储于此，后面的滤镜复用其纹理，而不是根据bitmap再生成纹理
 */
public class ImageOriginFilter extends AFilter {
    protected static final String TAG = "ImageOriginFilter";
    // private Bitmap mBitmap;
    private String mBitmapPath;
    private float[] cube;
    private int bitmapAngle;
    private boolean isDoRotate;
    private int matrixAngle;

    public ImageOriginFilter() {
        super();
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.uRes("shader/base_fragment.sh"));
        float[] coord = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        adjustImageScaling(width, height);
    }

    /**
     * 因为比例方案有三个
     * 方块，宽屏和竖屏，然后根据本身glview的大小设置showview的大小
     * 如果showview中纹理填充不玩，会拿其他颜色进行填充
     * <p>
     * 如果图片为空，则显示showview的大小
     */
    public void adjustImageScaling(int width, int height) {
        float outputWidth = width;
        float outputHeight = height;

        if (!TextUtils.isEmpty(mBitmapPath)) {
            Bitmap mBitmap = BitmapFactory.decodeFile(mBitmapPath);
            if (mBitmap == null) {
                return;
            }
            int framewidth = mBitmap.getWidth();
            int frameheight = mBitmap.getHeight();
            int rotation = (matrixAngle + bitmapAngle) % 360;
            if ((rotation == 90 || rotation == 270)) {
                framewidth = mBitmap.getHeight();
                frameheight = mBitmap.getWidth();
            }
            mBitmap.recycle();
            float ratio1 = outputWidth / framewidth;
            float ratio2 = outputHeight / frameheight;
            float ratioMax = Math.min(ratio1, ratio2);
            // 居中后图片显示的大小
            int imageWidthNew = Math.round(framewidth * ratioMax);
            int imageHeightNew = Math.round(frameheight * ratioMax);
            LogUtils.i(TAG, "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:"
                    + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
            // 图片被拉伸的比例,以及显示比例时的偏移量
            float ratioWidth = outputWidth / imageWidthNew;
            float ratioHeight = outputHeight / imageHeightNew;
            LogUtils.i(TAG, "isDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth + ",ratioHeight:" + ratioHeight);
            if (isDoRotate) {// 这个回路好难理解啊，根据结果推到没有旋转前的状态
                if (ratioWidth > ratioHeight) {
                    ratioHeight = ratioWidth;
                    ratioWidth = 1;
                    LogUtils.i(TAG, "ratioWidth>  ratioHeigh----tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                } else {
                    ratioWidth = ratioHeight;
                    ratioHeight = 1;
                    LogUtils.i(TAG, "ratioWidth < ratioHeight---tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                }
            }
            // 根据拉伸比例还原顶点
            cube = new float[] { pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                    pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight, };
            mVerBuffer.clear();
            mVerBuffer.put(cube).position(0);
        }
    }

    public void initTexture() {
        if (!TextUtils.isEmpty(mBitmapPath)) {
            GLES20.glDeleteTextures(1, new int[] { getTextureId() }, 0);
            setTextureId(OpenGlUtils.loadTexture(BitmapFactory.decodeFile(mBitmapPath), NO_TEXTURE, true));
        }
    }

    public void setBitmapPath(String path, int angle) {
        this.mBitmapPath = path;
        this.bitmapAngle = angle;
        setRotation(bitmapAngle);
    }

    /**
     * 图片旋转时，出现拉伸情况
     *
     * @param r
     */
    public void doRotaion(int r) {
        matrixAngle = r;
        isDoRotate = false;
        if (r == 90 || r == 270) {
            isDoRotate = true;
        }
    }

    /**
     * 旋转操作,根据草稿本记录的矩阵值进行赋值
     *
     * @param rotation
     */
    public void setVertexRotation(int rotation) {
        rotation = (360 - rotation) % 360;
        switch (rotation) {
        case ROT_0:
            pos = new float[] { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, };

            break;
        case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
            pos = new float[] { 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f };
            break;
        case ROT_180:
            pos = new float[] { 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f };
            break;
        case ROT_270:
            pos = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
            break;

        }
        mVerBuffer.clear();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
    }

    /**
     * 旋转操作
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        float[] coord = getCoord();
        switch (rotation) {
        case ROT_0:
            coord = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };

            break;
        case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
            // coord = new float[]{
            // 1.0f, 1.0f,
            // 0.0f, 1.0f,
            // 0.0f, 0.0f,
            // 1.0f, 0.0f
            // };
            coord = new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };

            break;
        case ROT_180:
            // coord = new float[]{
            // 1.0f, 1.0f,
            // 1.0f, 0.0f,
            // 0.0f, 1.0f,
            // 0.0f, 0.0f,
            // };
            coord = new float[] { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, };
            break;
        case ROT_270:
            coord = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
            break;

        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    public int getBitmapAngle() {
        return bitmapAngle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
