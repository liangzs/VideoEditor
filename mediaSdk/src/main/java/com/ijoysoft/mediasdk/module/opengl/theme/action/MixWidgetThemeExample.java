package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.opengl.GLES20;
import android.util.Log;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;

/**
 * 控件的纹理显示
 */
public class MixWidgetThemeExample extends BaseThemeExample {
    public static final String NO_FILTER_VERTEX_SHADER = "" + "attribute vec4 vPosition;\n" + "attribute vec2 vCoord;\n"
            + "uniform mat4 vMatrix; \n" + "varying vec2 textureCoordinate;\n" + " \n" + "void main()\n" + "{\n"
            + "    gl_Position =  vMatrix*vPosition;\n" + "    textureCoordinate = vCoord.xy;\n" + "}";

    private int mPrograssLocation;
    private int mDirecctionLocation;
    private float progress;
    private float[] enterFrames;

    public MixWidgetThemeExample(int totalTime, int enterTime, int stayTime, int outTime) {
        super(totalTime, enterTime, stayTime, outTime);
        stayAction = new StayAnimation(stayTime, AnimateInfo.STAY.SPRING_Y);
        outAnimation = new AnimationBuilder(outTime).setCoordinate(0, 0f, 2f, 2f).build();
        LogUtils.i(TAG, "onCreate");
        progress = 0;
        enterFrames = EvaluatorHelper.evaluateWidgetMixProgress(enterTime);
        status = ActionStatus.ENTER;
    }

    @Override
    protected void onCreate() {
//        createProgramByAssetsFile(NO_FILTER_VERTEX_SHADER,
//                OpenGlUtils.uRes("shader/base_fragment.sh"));
        createProgramByAssetsFile(NO_FILTER_VERTEX_SHADER,
                OpenGlUtils.readShaderFromRawResource(R.raw.theme_widge_mix_move));
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        mDirecctionLocation = GLES20.glGetUniformLocation(mProgram, "direction");
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }


    @Override
    public void drawFrame() {
        switch (status) {
            case ENTER:
//                GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
//                GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
//                GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_DST_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
                transitionOM = MatrixUtils.getOriginalMatrix();
                if (frameIndex < enterFrames.length) {
                    progress = enterFrames[frameIndex];
                    Log.i("drawframe", "progress:" + progress);
                    GLES20.glUniform2fv(mDirecctionLocation, 1, new float[]{0, 1}, 0);
                    GLES20.glUniform1f(mPrograssLocation, progress);
                }
                break;
            case STAY:
                if (stayAction != null) {
                    transitionOM = stayAction.draw();
                }
                break;
            case OUT:
                if (outAnimation != null) {
                    transitionOM = outAnimation.draw();
                }
                break;
        }
//        setMatrix(transitionOM);
        draw();
        frameIndex++;
        if (frameIndex > stayCount && status == ActionStatus.ENTER) {
            status = ActionStatus.STAY;
        }
        if (frameIndex > outCount && status == ActionStatus.STAY) {
            status = ActionStatus.OUT;
        }
    }


    private void mixDraw() {

    }
}
