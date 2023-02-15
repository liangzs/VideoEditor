package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;
import android.util.SparseArray;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Description:
 */
public abstract class AFilter {
    public static final int ROT_0 = 0;
    public static final int ROT_90 = 90;
    public static final int ROT_180 = 180;
    public static final int ROT_270 = 270;

    private static final String TAG = "Filter";


    public static boolean DEBUG = true;
    /**
     * 单位矩阵
     */
    public static final float[] OM = MatrixUtils.getOriginalMatrix();
    /**
     * 程序句柄
     */
    protected int mProgram;
    /**
     * 顶点坐标句柄
     */
    protected int mHPosition;
    /**
     * 纹理坐标句柄
     */
    protected int mHCoord;
    /**
     * 总变换矩阵句柄
     */
    protected int mHMatrix;
    /**
     * 默认纹理贴图句柄
     */
    protected int mHTexture;


    /**
     * 顶点坐标Buffer
     */
    protected FloatBuffer mVerBuffer;

    /**
     * 纹理坐标Buffer
     */
    protected FloatBuffer mTexBuffer;
    protected float alpha = 1.0f;
    protected int alphaLocation;

    /**
     * 索引坐标Buffer
     */

    protected int mFlag = 0;

    private float[] matrix = Arrays.copyOf(OM, 16);

    private int textureType = 0;      //默认使用Texture2D0
    //纹理id
    private int textureId = 0;
    /**
     * 顶点坐标
     * pos[0], pos[1] 左下点
     * pos[2], pos[3] 左上点
     * pos[4], pos[5] 右下点
     * pos[6], pos[7] 右上点
     */
    protected float[] pos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    //纹理坐标
    private float[] coord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    protected int POSITION_COMPONENT_COUNT = 2;
    private SparseArray<boolean[]> mBools;
    private SparseArray<int[]> mInts;
    private SparseArray<float[]> mFloats;
    //控制滤镜显示时间
    protected DurationInterval durationInterval;


    protected OnSizeChangedListener onSizeChangedListener = null;

    public AFilter() {
        initBuffer();
    }

    public final void create() {
        onCreate();
    }

    public final void setSize(int width, int height) {
        onSizeChanged(width, height);
    }

    public void draw() {
        onClear();
        onUseProgram();
        onSetExpandData();
        onBindTexture();
        onDrawArraysPre();
        onDraw();
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    public float[] getMatrix() {
        return matrix;
    }

    public final void setTextureType(int type) {
        this.textureType = type;
    }

    public final int getTextureType() {
        return textureType;
    }

    public final int getTextureId() {
        return textureId;
    }

    public final void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setFlag(int flag) {
        this.mFlag = flag;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFloat(int type, float... params) {
        if (mFloats == null) {
            mFloats = new SparseArray<>();
        }
        mFloats.put(type, params);
    }

    public void setInt(int type, int... params) {
        if (mInts == null) {
            mInts = new SparseArray<>();
        }
        mInts.put(type, params);
    }

    public void setBool(int type, boolean... params) {
        if (mBools == null) {
            mBools = new SparseArray<>();
        }
        mBools.put(type, params);
    }

    public boolean getBool(int type, int index) {
        if (mBools == null) return false;
        boolean[] b = mBools.get(type);
        return !(b == null || b.length <= index) && b[index];
    }

    public int getInt(int type, int index) {
        if (mInts == null) return 0;
        int[] b = mInts.get(type);
        if (b == null || b.length <= index) {
            return 0;
        }
        return b[index];
    }

    public float getFloat(int type, int index) {
        if (mFloats == null) return 0;
        float[] b = mFloats.get(type);
        if (b == null || b.length <= index) {
            return 0;
        }
        return b[index];
    }

    public int getOutputTexture() {
        return -1;
    }

    /**
     * 实现此方法，完成程序的创建，可直接调用createProgram来实现
     */
    protected abstract void onCreate();

    /**
     * 对屏幕变化进行监听
     *
     * @param width  屏宽
     * @param height 屏高
     */
    public void onSizeChanged(int width, int height) {
        if (onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(width, height);
        }
    }

    protected final void createProgram(String vertex, String fragment) {
        mProgram = uCreateGlProgram(vertex, fragment);
        mHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord = GLES20.glGetAttribLocation(mProgram, "vCoord");
        mHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        mHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        alphaLocation = GLES20.glGetUniformLocation(mProgram, "alpha");

    }

    protected final void createProgramByAssetsFile(String vertex, String fragment) {
        createProgram(vertex, fragment);
    }

    /**
     * Buffer初始化
     */
    protected void initBuffer() {
        ByteBuffer a = ByteBuffer.allocateDirect(32);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer = a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
        ByteBuffer b = ByteBuffer.allocateDirect(32);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer = b.asFloatBuffer();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    /**
     * 启用顶点坐标和纹理坐标进行绘制
     */
    protected void onDraw() {
        GLES20.glUniform1f(alphaLocation, alpha);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVerBuffer);
        GLES20.glEnableVertexAttribArray(mHCoord);
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHCoord);
    }

    protected void onUseProgram() {
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 清除画布
     */
    protected void onClear() {
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * 设置其他扩展数据
     */
    protected void onSetExpandData() {
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0);
    }

    /**
     * 绑定默认纹理
     */
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(mHTexture, textureType);
    }

    public static void glError(int code, Object index) {
        if (DEBUG && code != 0) {
            LogUtils.e(TAG, "glError:" + code + "---" + index);
        }
    }

    //创建GL程序
    public static int uCreateGlProgram(String vertexSource, String fragmentSource) {
        int vertex = uLoadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertex == 0) {
            LogUtils.v(AFilter.class.getSimpleName(), "create vertex failed:");
            return 0;
        }
        int fragment = uLoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment == 0) {
            LogUtils.v(AFilter.class.getSimpleName(), "create fragment failed:");
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertex);
            GLES20.glAttachShader(program, fragment);
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                glError(1, "Could not link program:" + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    /**
     * 加载shader
     */
    public static int uLoadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (0 != shader) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                glError(1, "Could not compile shader:" + shaderType);
                glError(1, "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
        textureId = -1;
    }


    public void setDurationInterval(DurationInterval durationInterval) {
        this.durationInterval = durationInterval;
    }

    public DurationInterval getDurationInterval() {
        return durationInterval;
    }


    public void setCoord(float[] coord) {
        this.coord = coord;
    }

    public float[] getCoord() {
        return this.coord;
    }

    /**
     * 获取当前顶点进行复用，因为对于显示区域有要求
     * 需要对顶点位置进行复用
     *
     * @return
     */
    public float[] getVertex() {
        return pos;
    }


    /**
     * 正常而已，在media工作台显示的区域中，除却背景位置的显示，其他位置都只针对显示区域进行显示
     * 那么就只需要通过顶点坐标去控制显示的区域
     *
     * @param vertex
     */
    public void setVertex(float[] vertex) {
        mVerBuffer.clear();
        mVerBuffer.put(vertex).position(0);
    }

    public void setVertex(float[] vertex, int positionCcount) {
        mVerBuffer.clear();
        mVerBuffer.put(vertex).position(0);
        POSITION_COMPONENT_COUNT = positionCcount;
    }

    public void setTexturVertex(float[] vertex) {
        mTexBuffer.clear();
        mTexBuffer.put(vertex).position(0);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
    }


    /**
     * 将容量为8的四个点数组转化为 左右上下 四个位置的数组
     *
     * @param pos 容量为8的点的坐标数组
     * @return 含有左右上下的数组
     */
    public static float[] convertPosToLeftRightTopBottom(float[] pos) {
        return new float[]{
                //left:
                pos[0],
                //right
                pos[4],
                //top
                pos[3],
                //bottom
                pos[1]
        };
    }

    /**
     * 将 左右上下 四个位置的数组 转化为 容量为8的四个点数组
     *
     * @param border 含有左右上下的数组
     * @return 容量为8的点的坐标数组
     */
    public static float[] convertLeftRightTopBottomToPos(float[] border) {
        return new float[]{
                border[0], border[3],
                border[0], border[2],
                border[1], border[3],
                border[1], border[2],
        };
    }

    /**
     * 将 左右上下 四个位置 转化为 容量为8的四个点数组
     *
     * @param left
     * @param right
     * @param top
     * @param bottom
     * @return 容量为8的点的坐标数组
     */
    public static float[] convertLeftRightTopBottomToPos(float left, float right, float top, float bottom) {
        return new float[]{
                left, bottom,
                left, top,
                right, bottom,
                right, top,
        };

    }

    /**
     * 获取相对坐标下的宽度
     *
     * @param pos 容量为8的点的坐标数组
     * @return 宽度
     */
    public static float getWidth(float[] pos) {
        return pos[4] - pos[0];
    }

    /**
     * 获取相对坐标下的高度
     *
     * @param pos 容量为8的点的坐标数组
     * @return 高度
     */
    public static float getHeight(float[] pos) {
        return pos[1] - pos[3];
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int width, int height);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    /**
     * 获取顶点中心
     */
    public static float[] getCubeCenter(float[] pos) {
        return new float[]{(pos[0] + pos[4]) / 2f, (pos[1] + pos[3]) / 2f};
    }

    /**
     * 顶点的上下左右四边的减法
     *
     * @param orientation
     * @param value
     * @return
     */
    public static void vertextMove(float[] pos, float value, AnimateInfo.ORIENTATION orientation) {
        switch (orientation) {
            case TOP:
                pos[1] += value;
                pos[3] += value;
                pos[5] += value;
                pos[7] += value;
                break;
            case BOTTOM:
                pos[1] -= value;
                pos[3] -= value;
                pos[5] -= value;
                pos[7] -= value;
                break;
            case LEFT:
                pos[0] += value;
                pos[2] += value;
                pos[4] += value;
                pos[6] += value;
                break;
            case RIGHT:
                pos[0] -= value;
                pos[2] -= value;
                pos[4] -= value;
                pos[6] -= value;
                break;
            case LEFT_TOP:
                vertextMove(pos, value, AnimateInfo.ORIENTATION.LEFT);
                vertextMove(pos, value, AnimateInfo.ORIENTATION.TOP);
                break;
            case RIGHT_TOP:
                vertextMove(pos, value, AnimateInfo.ORIENTATION.RIGHT);
                vertextMove(pos, value, AnimateInfo.ORIENTATION.TOP);
                break;
            case LEFT_BOTTOM:
                vertextMove(pos, value, AnimateInfo.ORIENTATION.LEFT);
                vertextMove(pos, value, AnimateInfo.ORIENTATION.BOTTOM);
                break;
            case RIGHT_BOTTOM:
                vertextMove(pos, value, AnimateInfo.ORIENTATION.RIGHT);
                vertextMove(pos, value, AnimateInfo.ORIENTATION.BOTTOM);
                break;
        }
    }
}
