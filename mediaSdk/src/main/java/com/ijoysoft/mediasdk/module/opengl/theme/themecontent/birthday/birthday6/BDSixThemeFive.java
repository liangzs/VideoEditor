package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

public class BDSixThemeFive extends BaseBlurThemeExample {

    private BaseThemeExample widgetOne;
    private float[] origin = new float[8];

    private float widgetEneterCount;
    private float widgetEneterIndex;
    private float[] posOneOffset = new float[8];//计算控件出现动画

    public BDSixThemeFive(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(3000, true, 2, 0.1f, 1.0f);
        stayAction.setZView(-2.5f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(1f, 0f, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS_RE, 270, 360).setZView(-2.5f).build();

        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(-2.5f).build();


        //widget
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1f, 0f, 0f).
                setIsWidget(true).setIsNoZaxis(true).build());
        widgetOne.setStayAction(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsWidget(true).setIsNoZaxis(true).build());
    }

    @Override
    public void drawWiget() {
        if (getStatus() == ActionStatus.STAY) {
            if (widgetEneterIndex < widgetEneterCount) {
                arrayAdd();
            } else if (widgetEneterIndex > widgetEneterCount && widgetEneterIndex < widgetEneterCount * 2) {
                arraySub();
            }
            widgetEneterIndex++;
            widgetOne.setVertex(origin);
        }
        widgetOne.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //one
        float scaleX = width < height ? 3 : (width == height ? 3 : 4);
        float centerX = width < height ? -0.4f : (width == height ? -0.4f : -0.6f);
        float centerY = width < height ? 0.8f : (width == height ? 0.78f : 0.75f);
        widgetOne.init(mimaps.get(0), width, height);
        origin = widgetOne.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);

        evaluateShade();
    }

    /**
     * 除了底端y的坐标不变化之外，涉及顶点坐标有：1,3,4,5,7,8 （即 2、6两个坐标不做变化)
     * 1-2两个顶点是负x轴，34是正x轴
     */
    private void evaluateShade() {
        float[] end = new float[8];
        end[0] = origin[0] + 0.05f;
        end[1] = 1f;
        //end[1]为顶点
        end[2] = origin[2] + 0.05f;
        end[3] = origin[3] - 0.05f;
        end[4] = origin[4] - 0.05f;
        end[5] = 1f;
        //end[5]为1顶点
        end[6] = origin[6] - 0.05f;
        end[7] = origin[7] - 0.05f;
        widgetEneterCount = (500 * ConstantMediaSize.FPS / 1000);
        for (int i = 0; i < end.length; i++) {
            posOneOffset[i] = (end[i] - origin[i]) / widgetEneterCount;
        }
    }

    private void arrayAdd() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] + posOneOffset[i];
        }
    }

    private void arraySub() {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin[i] - posOneOffset[i];
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
    }
}
