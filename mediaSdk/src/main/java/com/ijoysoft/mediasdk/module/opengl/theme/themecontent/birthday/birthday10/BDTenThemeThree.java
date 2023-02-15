package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday10;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

public class BDTenThemeThree extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private float[] origin = new float[8];
    private float widgetEneterCount;
    private float widgetEneterIndex;
    private float widgetStayCount;
    private float[] posOneOffset = new float[8];//计算控件出现动画
    private float[] posEnterOffset = new float[8];//计算控件出现动画
    private BaseThemeExample widgetBorder;


    public BDTenThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setZView(-2.5f).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(-2.5f).build();


        widgetBorder = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetBorder.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setScale(1.04f, 1.04f).setZView(-2.5f).build());
        widgetBorder.setStayAction(new AnimationBuilder(3000).setIsNoZaxis(true).setZView(0).build());
        widgetBorder.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).build());

    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, 2f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setStayAction(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.ENTER) {
            widgetBorder.setNoZaxis(false);
        } else {
            widgetBorder.setNoZaxis(true);
        }
        widgetBorder.drawFrame();
        if (frameIndex > stayCount - widgetEneterCount / 2 && frameIndex < stayCount + widgetEneterCount / 2) {
            arrayAdd(true);
        } else if (frameIndex >= widgetEneterCount / 2 && frameIndex < stayCount + widgetEneterCount * 1.5) {
            arraySub(true);
        } else {
            if (widgetEneterIndex < widgetStayCount) {
                arrayAdd(false);
            } else if (widgetEneterIndex > widgetStayCount && widgetEneterIndex < widgetStayCount * 2) {
                arraySub(false);
            }
            widgetEneterIndex++;
        }
        widgetOne.setVertex(origin);
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 3f : (width == height ? 3 : 3);
        float centerX = 0;
        float centerY = width < height ? 0.8f : (width == height ? 0.7f : 0.7f);
        widgetOne.init(mimaps.get(0), width, height);
        origin = widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
        evaluateShade();
        widgetBorder.init(mimaps.get(1), width, height);
        widgetBorder.setVertex(pos);
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private void evaluateShade() {
        float[] end = new float[8];
        end[0] = origin[0] - 0.05f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.05f;
        end[3] = origin[3] + 0.05f;
        end[4] = origin[4] + 0.05f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.05f;
        end[7] = origin[7] + 0.05f;
        widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
        widgetStayCount = (1000 * ConstantMediaSize.FPS / 1000);
        for (int i = 0; i < end.length; i++) {
            posEnterOffset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }

        end[0] = origin[0] - 0.04f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] - 0.04f;
        end[3] = origin[3] + 0.04f;
        end[4] = origin[4] + 0.04f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] + 0.04f;
        end[7] = origin[7] + 0.04f;
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - origin[i]) / widgetStayCount;
        }
    }

    private void arrayAdd(boolean isEnter) {
        if (isEnter) {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] + posEnterOffset[i];
            }
        } else {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] + posOneOffset[i];
            }
        }

    }

    private void arraySub(boolean isEnter) {
        if (isEnter) {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] - posEnterOffset[i];
            }
        } else {
            for (int i = 0; i < origin.length; i++) {
                origin[i] = origin[i] - posOneOffset[i];
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetBorder.onDestroy();
    }
}
