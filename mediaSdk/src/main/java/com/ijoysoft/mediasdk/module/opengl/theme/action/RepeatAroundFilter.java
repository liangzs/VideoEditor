package com.ijoysoft.mediasdk.module.opengl.theme.action;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;

import java.util.Arrays;

/**
 * 重复环绕渲染图片
 * @author hayring
 * @date 2021/12/3  9:15
 */
public class RepeatAroundFilter extends ImageOriginFilter {

    /**
     * 相位在原生代码中的位置
     */
    protected int vPhaseLocation;

    /**
     * 相位
     */
    protected float phase = 0f;


    /**
     * 相位变化单位
     */
    protected float offsetPhase = 0.01f;

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/repeat_vertex.glsl"),
                OpenGlUtils.uRes("shader/repeat_fragment.glsl"));
        vPhaseLocation = GLES20.glGetAttribLocation(mProgram, "phase");


        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
        cube = pos;
    }




    /**
     * 初始化素材资源
     * @param bitmap 控件素材
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param yPos y轴位置
     */
    public void init(Bitmap bitmap, int width, int height, float yPos) {
        if (bitmap == null || bitmap.isRecycled()) {
            LogUtils.e(TAG, "bitmap = null or recyle");
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            initTexture(bitmap);
        }
        create();
//        setSize(width, height);
        adjustScaling(width, height, yPos);
    }

    /**
     * 初始化素材资源
     * @param bitmap 控件素材
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param orientation 置顶或置底
     */
    public void init(Bitmap bitmap, int width, int height, AnimateInfo.ORIENTATION orientation) {
        if (bitmap == null || bitmap.isRecycled()) {
            LogUtils.e(TAG, "bitmap = null or recyle");
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            initTexture(bitmap);
        }
        create();
//        setSize(width, height);
    }

    /**
     * 图像
     */
    protected Bitmap bitmap;
    
    protected int originTexture;

    public void initTexture(Bitmap bitmap) {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
        if (originTexture == -1) {
            LogUtils.e(TAG, "生成纹理失败！！！" + bitmap.getWidth() + "," + bitmap.getHeight());
            originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
            LogUtils.e(TAG, "生成纹理失败:" + originTexture);
        } else {
                //频繁打印的日志放到-verbose 级别中
                LogUtils.v(TAG, "生成纹理成功！！！" + bitmap.getWidth() + "," + bitmap.getHeight());
        }
        setTextureId(originTexture);
        this.bitmap = bitmap;
    }


    /**
     * 上下边，靠边缩放,目前只针对上下边
     */
    public float[] adjustScaling(int showWidth, int showHeight, float yPos) {
        float ratio1 = (float) showWidth / bitmap.getWidth();
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioHeight = bitmap.getHeight() * ratio1 / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = pos;
        cube = new float[]{-1, yPos + ratioHeight, -1, yPos - ratioHeight,
                1, yPos + ratioHeight, 1, yPos - ratioHeight};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }

    /**
     * 上下边，靠边缩放,目前只针对上下边
     */
    public float[] adjustScaling(int showWidth, int showHeight, AnimateInfo.ORIENTATION orientation) {
        float ratio1 = (float) showWidth / bitmap.getWidth();

        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioHeight = bitmap.getHeight() * ratio1 / (float) showHeight;


        // 根据拉伸比例还原顶点
        cube = pos;
        if (orientation == AnimateInfo.ORIENTATION.TOP) {
            cube = new float[]{-1, -1 + 2 * ratioHeight, -1, -1,
                    1, -1 + 2 *ratioHeight, 1, -1};
        } else if (orientation == AnimateInfo.ORIENTATION.BOTTOM) {
            cube = new float[]{-1, 1, -1, 1 - 2 * ratioHeight,
                    1, 1, 1, 1 - 2 * ratioHeight};
        } else {
            throw new IllegalArgumentException("only support TOP and BOTTOM");
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }



    /**
     * float ratioWidth = imageWidthNew / outputWidth; 相当于归一化坐标，然后 ratioWidth / 4,
     * ratioHeight / 8是换算到总的归一化坐标
     *
     * @param showWidth   渲染宽度
     * @param showHeight  渲染高度
     * @param framewidth  图片（控件）宽度
     * @param frameheight 图片（控件）高度
     * @param centerX     中点x轴相对坐标
     * @param centerY     中点y轴相对坐标
     * @param scalex      x轴缩放的程度（比例的倒数）
     * @param scaley      y轴缩放的程度（比例的倒数）
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY, float scalex, float scaley) {
        //图片宽高相对渲染宽高缩小的程度
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        //哪一个轴缩放的程度最小，就说明等比例拉伸后该轴长先达到渲染(宽/高)的长度
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        /**
         * @author hayring将该图片等比拉伸至刚才测量的值，即某一方向上的长度先达到与渲染（长/宽）相等
         */
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v("adjustScaling", "outputWidth:" + (float) showWidth + ",outputHeight:" + (float) showHeight + ",imageWidthNew:"
//                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = new float[]{centerX - ratioWidth / scalex, centerY + ratioHeight / scaley, centerX - ratioWidth / scalex, centerY - ratioHeight / scaley,
                centerX + ratioWidth / scalex, centerY + ratioHeight / scaley, centerX + ratioWidth / scalex, centerY - ratioHeight / scaley};
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


    /**
     * 设置值
     */
    @Override
    public void draw() {
        phase += offsetPhase;
        GLES20.glVertexAttrib1f(vPhaseLocation, phase);
        super.draw();
    }


    /**
     * 相位变化单位设置
     * @param phase 相位变化单位
     */
    public void setPhase(float phase) {
        this.phase = phase;
    }
}
