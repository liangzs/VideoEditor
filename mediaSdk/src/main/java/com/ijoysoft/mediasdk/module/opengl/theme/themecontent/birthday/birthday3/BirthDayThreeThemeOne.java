package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle.BDThreeParticleOneDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle.BDThreeWidgetOneSystem;

import java.util.List;

/**
 * 上进，上下悬停，放大，左划出
 */
public class BirthDayThreeThemeOne extends BDThreeBaseExample {
    private BaseThemeExample widgetOne;
    //烟花绘画
    private BDThreeParticleOneDrawer particleDrawer;


    public BirthDayThreeThemeOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 2000, 1000, DEFAULT_OUT_TIME);
        stayAction = new StayAnimation(2000, AnimateInfo.STAY.SPRING_Y, -2);
        stayNext = new AnimationBuilder(weightTime).setScale(1.0f, 1.2f).setZView(-2).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -2.5f, 0, 0).setZView(-2).build();
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0, 0f, -2f, 0).setZView(-2).setScale(1.2f, 1.2f));
        setZView(-2);
        particleDrawer = new BDThreeParticleOneDrawer(new BDThreeWidgetOneSystem(1500));
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, -1.5f, 0f, 0f).setZView(-3).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-3).setCoordinate(0, 0f, -2.5f, 0f).build());
    }

    @Override
    protected void drawFramePre() {
        widgetOne.drawFrame();
    }

    @Override
    protected void drawStayNextLater() {
        particleDrawer.onDrawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        //顶点坐标
        float pos[] = {
                -1.2f, -0.8f,
                -1.2f, -1.25f,
                1.2f, -0.8f,
                1.2f, -1.25f,
        };
        widgetOne.setVertex(pos);
        particleDrawer.init(R.raw.bdthree_cluster_one_vertex, R.raw.bdthree_cluster_one_fragment, mimaps.get(1));
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        particleDrawer.onSurfaceCreated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        if (particleDrawer != null) {
            particleDrawer.onDestroy();
        }
    }

    @Override
    public String printOutString() {
        return this.getClass().getName() + ":getTexture:" + getTextureId() + ",wighet_one:" + widgetOne.getTexture();
    }
}
