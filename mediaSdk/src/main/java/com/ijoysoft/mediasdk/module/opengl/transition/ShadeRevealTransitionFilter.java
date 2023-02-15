package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 揭开动作形状
 */
public class ShadeRevealTransitionFilter extends TransitionFilter {

    private int mPrograssLocation;
    private int reverseLocation;
    private boolean isReverse;

    private static final String FRAGMENT = FRAGMENT_HEAD +
            "uniform int reverse;\n" +
            "vec2 scale(vec2 textureCoordinate, vec2 center, vec2 scaleRatio)\n" +
            "{\n" +
            "vec2 coord = textureCoordinate;\n" +
            "coord = coord - center;\n" +
            "coord = coord / scaleRatio;\n" +
            "coord = coord + center;\n" +
            "return coord;\n" +
            "}" +
            "void main() {\n" +
            "float w=20.0;" +
            "float alpha=0.0;\n" +
            "vec4 resColor=vec4(0.0,0.0,0.0,0.0);" +
            "if (progress <= 0.5) {\n" +
            " vec2 scaleValue = vec2(1.0 + 0.2 * progress);\n" +
            " vec4 texColor1 = texture2D(vTexture1, scale(textureCoordinate, vec2(0.25, 0.5), scaleValue));\n" +
            "   float time = progress * 2.0;//0-1整体过程\n" +
            "  if(reverse==0){\n" +
            "  alpha = clamp(-w * textureCoordinate.x + time*(w + 1.0 ), 0.0, 1.0);\n" +
            "   resColor = mix( texColor1, vec4(1.0), alpha);\n" +
            "  }else{\n" +
            "  alpha = clamp(-w * textureCoordinate.x +w+1.0 +time*(-w - 1.0 ), 0.0, 1.0);\n" +
            "   resColor = mix( vec4(1.0),texColor1,  alpha);\n" +
            "  }\n" +

            " }\n" +
            "  else {\n" +
            "   vec2 scaleValue = vec2(1.1 - (progress - 0.5) * 0.2);\n" +
            "   vec4 texColor2 = texture2D(vTexture, scale(textureCoordinate, vec2(0.75, 0.5), scaleValue));\n" +
            "   float time = 2.0 * abs(progress - 1.0);\n" +
            "   if(reverse==0){\n" +
            "   alpha = clamp(-w * textureCoordinate.x + time*(w + 1.0 ), 0.0, 1.0);\n" +
            "   resColor = mix(texColor2, vec4(1.0), alpha);\n" +
            "   }else{\n" +
            "   alpha = clamp(-w * textureCoordinate.x +w+1.0+ time*(-w - 1.0 ), 0.0, 1.0);\n" +
            "   resColor = mix(vec4(1.0),texColor2 , alpha);\n" +
            " }\n" +

            " }" +
            "gl_FragColor = resColor;" +
            "}";


    public ShadeRevealTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);
        switch (transitionType) {
            case SHADE_REVEAL:
                isReverse = false;
                break;
            case SHADE_REVEAL_RE:
                isReverse = true;
                break;
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        reverseLocation = GLES20.glGetUniformLocation(mProgram, "reverse");
        progress = 0;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > TRANSITION_PROGRESS) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        if (isReverse) {
            GLES20.glUniform1i(reverseLocation, 1);
        } else {
            GLES20.glUniform1i(reverseLocation, 0);
        }
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
}
