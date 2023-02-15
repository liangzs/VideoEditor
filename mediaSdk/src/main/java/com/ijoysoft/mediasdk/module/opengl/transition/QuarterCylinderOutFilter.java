package com.ijoysoft.mediasdk.module.opengl.transition;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EmbeddedFilter;

import java.io.Serializable;

/**
 * 转场应该作用于全显示范围，即showwidth、showHeight，所以定点坐标应该是满的，然后viewport应该赋值为showWidth,showHeight
 * @author hayring
 * fork from {@link TransitionFilter}
 */
public class QuarterCylinderOutFilter extends EmbeddedFilter {


    /**
     * 往右边出去
     */
    private boolean right;

    /**
     * 往右边出去
     * 参数在程序中的位置
     */
    private int mRightLocation;



    /**
     * z轴距离
     * 参数在程序中的位置
     */
    private int mZAxisLocation;

    /**
     * z轴距离
     */
    private float zAxis = 0.4f;




    public QuarterCylinderOutFilter() {
        mVertexShader = OpenGlUtils.uRes(VERTEX_PATH);
        mFragmentShader = OpenGlUtils.uRes(FRAGMENT_PATH);
    }

    private static final String VERTEX_PATH = "shader/quarter_cylinder_vertex.glsl";

    private static final String FRAGMENT_PATH = "shader/quarter_cylinder_fragment.glsl";


    @Override
    public void onCreate() {
        super.onCreate();
        mZAxisLocation = GLES20.glGetUniformLocation(mProgram, "uMaxZaxis");
        mRightLocation = GLES20.glGetUniformLocation(mProgram, "u_Right");
        LogUtils.i(getClass().getSimpleName(), "onCreate");
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1f(mZAxisLocation, zAxis);
        GLES20.glUniform1f(mRightLocation, right ? 1f : 0f);

    }







    public void setzAxis(float zAxis) {
        this.zAxis = zAxis;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
