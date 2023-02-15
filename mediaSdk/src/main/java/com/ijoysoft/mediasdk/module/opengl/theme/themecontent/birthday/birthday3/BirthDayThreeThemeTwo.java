package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WaveAnimation;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle.BDThreeParticleOneDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle.BDThreeWidgetOneSystem;

import java.util.List;

/**
 * 右边波浪进
 */
public class BirthDayThreeThemeTwo extends BaseThemeExample {

    //烟花绘画
    private BDThreeParticleOneDrawer particleDrawer;
    private BaseThemeExample widgetOne;

    public BirthDayThreeThemeTwo(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new StayScaleAnimation(weightTime, false, 2, 0.1f, 1.1f);
        stayAction.setZView(-2);
        enterAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(2f, 0f, 0, 0).setZView(-2).setScale(1.1f, 1.1f));
        outAnimation = new WaveAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).
                setCoordinate(0f, 0f, -2f, 0).setZView(-2).setScale(1.1f, 1.1f));
        setZView(-2);

        particleDrawer = new BDThreeParticleOneDrawer(new BDThreeWidgetOneSystem(2000));
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME, true);
        widgetOne.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 1.0f, 0f, 0f).setIsNoZaxis(true).setZView(0).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(0).setIsNoZaxis(true).setFade(1f, 0f).build());
        widgetOne.setZView(0);
    }

    @Override
    public void drawWiget() {
        widgetOne.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        widgetOne.init(mimaps.get(0), width, height);
        float scale = width < height ? 2 : 2f;
        widgetOne.adjustScaling(width, height / 2, mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), AnimateInfo.ORIENTATION.BOTTOM, scale);
        particleDrawer.init(R.raw.bdthree_cluster_one_vertex, R.raw.bdthree_cluster_one_fragment, mimaps.get(1));
    }


    @Override
    public void drawFrame() {
        super.drawFrame();
        if (getStatus() == ActionStatus.STAY) {
            particleDrawer.onDrawFrame();
        }
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
        if (particleDrawer != null) {
            particleDrawer.onDestroy();
        }
        widgetOne.onDestroy();
    }

    @Override
    public String printOutString() {
        return this.getClass().getName() + ":getTexture:" + getTextureId() + ",wighet_one:" + widgetOne.getTexture();
    }
}
