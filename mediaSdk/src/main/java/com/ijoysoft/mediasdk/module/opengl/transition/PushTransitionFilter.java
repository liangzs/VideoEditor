package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import androidx.annotation.IntDef;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ijoysoft.mediasdk.module.opengl.transition.PushTransitionFilter.PushType.B2T;
import static com.ijoysoft.mediasdk.module.opengl.transition.PushTransitionFilter.PushType.L2R;
import static com.ijoysoft.mediasdk.module.opengl.transition.PushTransitionFilter.PushType.R2L;
import static com.ijoysoft.mediasdk.module.opengl.transition.PushTransitionFilter.PushType.T2B;

/**
 * 推入，同移动
 */
public class PushTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int typeLocation;
    private int type;

    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform int type;\n" +
            "void main() {\n" +
            "vec4 resColor = vec4(1.0,0.0,0.0,1.0);\n" +
            " float R = 1.0 - progress;\n" +
            "if (type == 0) {\n" +
            "       if (textureCoordinate.x >= progress) {\n" +
            "           resColor = texture2D(vTexture1, vec2(textureCoordinate.x - progress, textureCoordinate.y));\n" +
            "       }\n" +
            "       else {\n" +
            "           resColor = texture2D(vTexture, vec2(textureCoordinate.x + 1.0 - progress, textureCoordinate.y));\n" +
            "       }\n" +
            "   }\n" +
            "   else if (type == 1) {\n" +
            "    if (textureCoordinate.x >= R){\n" +
            "        resColor = texture2D(vTexture, vec2(textureCoordinate.x - R, textureCoordinate.y));\n" +
            "    }\n" +
            "    else{\n" +
            "        resColor = texture2D(vTexture1, vec2(textureCoordinate.x - R + 1.0, textureCoordinate.y));\n" +
            "    }\n" +
            "   }\n" +
            "   else if (type == 2) {\n" +
            "       if (textureCoordinate.y >= progress) {\n" +
            "           resColor = texture2D(vTexture1, vec2(textureCoordinate.x, textureCoordinate.y - progress));\n" +
            "       }\n" +
            "       else {\n" +
            "           resColor = texture2D(vTexture, vec2(textureCoordinate.x,textureCoordinate.y+1.0- progress));\n" +
            "       }\n" +
            "   }\n" +
            "   else if (type == 3) {\n" +
            "       if (textureCoordinate.y <= R) {\n" +
            "           resColor = texture2D(vTexture1, vec2(textureCoordinate.x, textureCoordinate.y + progress));\n" +
            "       }\n" +
            "       else {\n" +
            "           resColor = texture2D(vTexture, vec2(textureCoordinate.x, textureCoordinate.y  +progress- 1.0));\n" +
            "       }\n" +
            "   }\n" +
            "gl_FragColor = resColor;" +
            "}";


    public PushTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        switch (transitionType) {
            case MOVE_PUSH_L:
                type = L2R;
                break;
            case MOVE_PUSH_R:
                type = R2L;
                break;
            case MOVE_PUSH_B:
                type = B2T;
                break;
            case MOVE_PUSH_T:
                type = T2B;
                break;
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        typeLocation = GLES20.glGetUniformLocation(mProgram, "type");
        progress = 0;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            GLES20.glUniform1f(mPrograssLocation, 1.0f);
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        GLES20.glUniform1i(typeLocation, type);
        progress += 0.04;
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v(TAG, "onDrawArraysPre<-" + progress);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void seekTo(int duration) {
        float d = (float) duration / 1000.0f;
        if (d >= 2.0f) {
            progress = 1.0f;
        } else {
            progress = d / 2;
        }
        LogUtils.i(TAG, "seekTo:" + progress + ",origin:" + duration);
        GLES20.glUniform1f(mPrograssLocation, progress);
    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }

    @IntDef({L2R, R2L, B2T, T2B})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PushType {
        int L2R = 0;
        int R2L = 1;
        int B2T = 2;
        int T2B = 3;
    }


}
