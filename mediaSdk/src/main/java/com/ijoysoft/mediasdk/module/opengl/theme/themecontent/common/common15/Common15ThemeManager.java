package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IMoveActionWrapper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustomNoOffset;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 该主题用了重度的动感模糊，所以重新定义模糊方案
 */
public class Common15ThemeManager extends BaseTimeThemeManager implements IBaseTimeCusFragmeng {
    private final String CUS_FRAGMENT = "  #define PITwo 6.28318530718//2pi\n" +
            "    #define PI 3.141592653589793\n" +
            "    precision mediump float;\n" +
            "    varying vec2 textureCoordinate;\n" +
            "    varying float vAlpha;\n" +
            "    uniform sampler2D vTexture;\n" +
            "    uniform float range;   //0-10 半径\n" +
            "    uniform float angle;//0-180 角度\n" +
            "\tuniform int blurType;\n" +
            "\tuniform float radioStrength;\n" +
            "    void main(){ \n" +
            "    float rad=PI/180.0*angle;\n" +
            "\tif(blurType==2){\n" +
            "\t  vec2 center = vec2(.5, .5);\n" +
            "\t  vec3 color = vec3(0.0);\n" +
            "\t  float total = 0.0;\n" +
            "\t  vec2 toCenter = center - textureCoordinate;\n" +
            "\t  for (float t = 0.0; t <= 20.0; t++) {\n" +
            "\t   float percent = (t) / 20.0;\n" +
            "\t   float weight = 1.0 * (percent - percent * percent);\n" +
            "\t   color += texture2D(vTexture, textureCoordinate + toCenter * percent * radioStrength).rgb * weight;\n" +
            "\t   total += weight;\n" +
            "\t  }\n" +
            "\t  gl_FragColor = vec4(color / total, 1.0);\n" +
            "\t  return;\n" +
            "\t };\n" +
            "    if(range==0.0){\n" +
            "    gl_FragColor=texture2D(vTexture, textureCoordinate);\n" +
            "    return;   \n" +
            "\t}\n" +
            "    vec4 clraverge=vec4(0.0);\n" +
            "    float samplerPre=1.0;\n" +
            "    for(float j = 1.0; j <= range; j+=samplerPre){\n" +
            "        float dx=0.01*cos(rad);\n" +
            "        float dy=0.01*sin(rad);\n" +
            "        vec2 samplerTexCoord = vec2(textureCoordinate.x+j*dx, textureCoordinate.y+j*dy);\n" +
            "        vec2 samplerTexCoord1= vec2(textureCoordinate.x-j*dx, textureCoordinate.y-j*dy);\n" +
            "        vec4 tc= texture2D(vTexture, samplerTexCoord);\n" +
            "        vec4 tc1= texture2D(vTexture, samplerTexCoord1);\n" +
            "        clraverge+=tc;\n" +
            "        clraverge+=tc1;\n" +
            "    }\n" +
            "    clraverge/=(range*2.0/samplerPre);\n" +
            "    gl_FragColor=clraverge;\n" +
            "\t}";
    private int rangeLocation;
    private int angleLocation;

    public Common15ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }


    @Override
    protected boolean blurBackground() {
        return true;
    }

    @Override
    public boolean isTextureInside() {
        return true;
    }


    /**
     * 添加 自定义fragment
     *
     * @return
     */
    @Override
    protected String customFragment() {
        setBaseTimeCusFragmeng(this);
        return CUS_FRAGMENT;
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * //左右移动
     * case 0:
     * return 800;
     * case 6:
     * return 1200;
     * default:
     * return 600;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
//        index = 8;
        currentDuration = 600;
        switch (index % createActions()) {
            case 0:
                currentDuration = 800;
            case 3:
            case 9:
                //右下角弹动，弹动角度归正
                list.addAll(EvaluatorHelper.Bottom2SpringYBycornorRight(currentDuration, duration, width, height));
                break;
            case 1:
                //左进
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setCusAction(new EaseOutMultiple(5))
                        .setCusStartEnd(10, 0)
                        .setIsSprintX(true)
                        .setMoveActionX(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15(x, 0.6f, -1)))
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
            case 2:
                //右进
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setCusAction(new EaseOutMultiple(5))
                        .setCusStartEnd(10, 0)
                        .setIsSprintX(true)
                        .setMoveActionX(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15(x, 0.6f, 1)))
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                break;
            case 4:
            case 10:
            case 11:
                //左下角弹动，带角度弹动，角度中心点为左下角
                list.addAll(EvaluatorHelper.Bottom2SpringYBycornorLeft(currentDuration, duration, width, height));
                break;
            case 5:
                //径向模糊放大
                list.add(new AnimationBuilder((int) (currentDuration * 0.7), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(1, 0, false)
                        .setScale(1.0f, 1.5f)
                        .build());
                list.add(new AnimationBuilder((int) (duration - currentDuration * 0.7), width, height, true).build());
                break;
            case 6:
                currentDuration = 1200;
                //缩放进来，z轴spring旋转
                list.add(new AnimationBuilder((int) (currentDuration * 0.6f), width, height, true)
                        .setIsSprintX(true)
                        .setScaleAction(new EaseOutCubic())
                        .setScale(1.3f, 1.0f)
                        .setMoveActionX(new IMoveActionWrapper() {
                            @Override
                            public float[] action(float duration, float start, float end) {
                                //定义自定域在0-1.5之间
                                int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
                                float[] frames = new float[framesNum];
                                float x;
                                for (int i = 0; i < framesNum; i++) {
                                    x = i * 1.0F / (framesNum - 1);
                                    x = x * 0.4f + 0.1f;
                                    frames[i] = Easings.easeSpringCommon15A(x, 0.1f, 1);
                                }
                                LogUtils.v("", Arrays.toString(frames));
                                return frames;
                            }
                        })
                        .build());
                list.add(new AnimationBuilder((int) (currentDuration * 0.4f), width, height, true)
                        .setRotateAction(new IMoveActionWrapper() {
                            @Override
                            public float[] action(float duration, float start, float end) {
                                //定义自定域在0.6~1之间
                                int framesNum = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
                                float[] frames = new float[framesNum];
                                float x;
                                for (int i = 0; i < framesNum; i++) {
                                    x = i * 1.0F / (framesNum - 1);
                                    x = x * 0.4f + 0.6f;
                                    frames[i] = Easings.easeSpring(x) * 0.8f;
                                }
                                LogUtils.v("", Arrays.toString(frames));
                                return frames;
                            }
                        })
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }

                break;
            case 7:
                //z轴srping 缩放进入
                //缩放进来，z轴spring旋转
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setScaleAction(new EaseOutCubic())
                        .setScale(1.7f, 1.0f)
                        .setRotateAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15B(x, 5, 1)))
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                break;

            case 8:
                // 从上下来弹动，
                list.add(new AnimationBuilder((int) (currentDuration), width, height, true)
                        .setRotationType(AnimateInfo.ROTATION.Z_ANXIS)
                        .setIsSprintY(true)
                        .setMoveAction(new EaseCustomNoOffset(x -> Easings.easeSpringCommon15(x, 1f, -1)))
                        //模糊
                        .setCusAction(new EaseOutMultiple(5))
                        .setCusStartEnd(10, 0)
                        .setCusAction2(new EaseOutMultiple(5))
                        .setCusStartEnd2(90, 90)
                        .build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                break;

        }
        return list;
    }

    @Override
    protected int createActions() {
        return 12;
    }


    @Override
    public void initLocation(int programe) {
        rangeLocation = GLES20.glGetUniformLocation(programe, "range");
        angleLocation = GLES20.glGetUniformLocation(programe, "angle");
    }

    @Override
    public void drawLocation(BaseEvaluate baseEvaluate) {
//        LogUtils.v("", "baseEvaluate.getCustomValue():" + baseEvaluate.getCustomValue());
        GLES20.glUniform1f(rangeLocation, (int) baseEvaluate.getCustomValue());
        GLES20.glUniform1f(angleLocation, baseEvaluate.getCustomValue2());
    }
}
