package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.FireworkBoomDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInPow;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutPow;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.SpringYEnter;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat1.Beat1ParticleSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Common10ThemeManager extends BaseTimeThemeManager implements IBaseTimeCusFragmeng {
    private final String CUS_LOCATION =
            "uniform float vProgress;//0-0.1f  \n" +
                    "uniform vec2 iResolution;\n" +
                    "uniform int showBoom;\n";
    private final String CUS_METHOD = " float circ(vec2 p,float progress) {\n" +
            "\tfloat r = length(p);\n" +
            "    r = sqrt(r);\n" +
            "    return abs(4.*r * fract(progress));\n" +
            "}\n";
    private final String CUS_BODY =
            "if(showBoom==1){\n" +
                    "vec2 uv = vec2(gl_FragCoord.x- 0.7*iResolution.x,gl_FragCoord.y - 0.2*iResolution.y) / iResolution.x;\n" +
                    " vec3 color = vec3(.0);\n" +
                    " float rz = abs(circ(uv,vProgress));   \n" +
                    " color = vec3(.1) / rz;   \n" +
                    "resultColor =resultColor+vec4(color*vec3(1.,1.,0.7), 1.0);\n" +
                    "}\n";

    private FireworkBoomDrawer fireworkBoomDrawer;
    private BaseEvaluate boomEvaluate;
    private int flashCount;
    private int progressLocation;
    private int iResolutionLocation;
    private int showBoomLocation;


    public Common10ThemeManager() {
        List<Bitmap> list = new ArrayList<>();
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle0));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle2));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle3));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle4));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle5));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle6));
//        list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common10_particle10));
        fireworkBoomDrawer = new FireworkBoomDrawer(new ParticleBuilder()
                .setListBitmaps(list)
                .setParticleCount(60)
                .setPointSize(3, 8)
                .setAlpha(false)
                .setiAddParticle(new Beat1ParticleSystem()));
        fireworkBoomDrawer.setInfo(0, 0, computeThemeCycleTime());
//        fireworkBoomDrawer.setInfo(0.7f, -0.7f, computeThemeCycleTime());
        List<ThemeWidgetInfo> themeWidgetInfos = new ArrayList<>();
        themeWidgetInfos.add(new ThemeWidgetInfo(0, 600));
        //480
        themeWidgetInfos.add(new ThemeWidgetInfo(600, 1080));
        themeWidgetInfos.add(new ThemeWidgetInfo(1080, 1560));
        themeWidgetInfos.add(new ThemeWidgetInfo(1560, 2040));
        fireworkBoomDrawer.setShowTime(themeWidgetInfos);
    }

    @Override
    protected int computeThemeCycleTime() {
        return 4440;
    }


    @Override
    protected boolean blurBackground() {
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
        StringBuffer sb = new StringBuffer();
        sb.append(BaseTimeThemeExample.FRAGMENT_HEAD).append(CUS_LOCATION)
                .append(CUS_METHOD).append(BaseTimeThemeExample.FRAGMENT_MAIN)
                .append(BaseTimeThemeExample.FRAGMENT_BODY).append(CUS_BODY)
                .append(BaseTimeThemeExample.FRAGMENT_END_BLACKET);
        LogUtils.v("", sb.toString());
        return sb.toString();
    }

    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        fireworkBoomDrawer.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        fireworkBoomDrawer.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
    }

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        //粒子的切换
        fireworkBoomDrawer.reset();
        flashCount = 0;
        switch (index % createActions()) {
            case 0:
            case 1:
            case 2:
            case 3:
                flashCount = (int) Math.ceil(mediaItem.getDuration() * ConstantMediaSize.FPS / 1000f);
                boomEvaluate = new AnimationBuilder((int) mediaItem.getDuration())
                        .setCusStartEnd(0.1f, 0.2f)
                        .build();
                break;
        }

    }

    @Override
    public void onDrawFrame(int currenPostion) {
        super.onDrawFrame(currenPostion);
//        fireworkBoomDrawer.onDrawFrame(currenPostion);
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * case 0:
     * return 600;
     * case 1:
     * case 2:
     * case 3:
     * return 480;
     * case 4:
     * return 2400;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
//        index = 4;
        currentDuration = 480;
        switch (index % createActions()) {
            case 0:
                currentDuration = 600;
                //600ms
                //旋转缓慢进入
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setScale(1.5f, 1.5f).
                        setRotaCenter(0, -0.7f).setMoveAction(new EaseOutCubic(true, false, true)).setRotateAction(new EaseOutPow()).
                        setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, -4, 4).setCoordinate(-0.2f, 0, 0.2f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //左出
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setMoveAction(new EaseInMultiple(5, true, false, false))
                        .setCoordinate(0.2f, 0, -1.5f, 0).setConors(AnimateInfo.ROTATION.Z_ANXIS, 4, -8)
                        .setRotateAction(new EaseInPow()).setRotaCenter(0, -0.7f)
                        .build());
                break;
            case 1:
                //左进
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setScale(1.5f, 1.5f).
                        setRotaCenter(0, -0.7f).setMoveAction(new EaseOutCubic(true, false, true)).setMoveBlurRange(10, 1)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -4, 4).setCoordinate(0.2f, 0, -0.2f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //右出
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setMoveAction(new EaseInMultiple(5, true, false, false))
                        .setCoordinate(-0.2f, 0, 2f, 0).setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 4, -4).setMoveBlurRange(0, 0)
                        .setRotateAction(new EaseInPow()).setRotaCenter(0, -0.7f)
                        .build());
                break;
            case 2:
                //左进
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setScale(1.5f, 1.5f).
                        setRotaCenter(0, -0.7f).setMoveAction(new EaseOutCubic(true, false, true)).setMoveBlurRange(10, 1)
                        .setConors(AnimateInfo.ROTATION.Z_ANXIS, -4, 4).setCoordinate(0.2f, 0, -0.2f, 0).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //下出
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setMoveAction(new EaseInCubic(true, false, false))
                        .setCoordinate(-0.2f, 0, -0.2f, 2).setMoveBlurRange(0, 10).build());
                break;
            case 3:
                //中心点模糊出现
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setMoveBlurRange(ActionBlurType.RATIO_BLUR, 10, 0)
                        .setMoveAction(new EaseOutCubic(false, true, false)).setScale(1.2f, 1.4f).build());
                if (duration > currentDuration) {
                    list.add(new AnimationBuilder((int) (duration - currentDuration), width, height, true)
                            .build());
                }
                //模糊消失
                list.add(new AnimationBuilder((int) (currentDuration / 2), width, height, true).setFade(1.4f, 1.2f).
                        setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 2).setFade(1.0f, 0.5f)
                        .build());
                break;
            case 4:
                //弹簧效果，针对y轴的弹簧效果
                list.add(new AnimationBuilder((int) duration, width, height, true).setScale(1.2f, 1.0f).setScaleAction(new EaseOutMultiple(5, false, true, false))
                        .setMoveAction(new SpringYEnter()).setIsSprintY(true).build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 5;
    }


    @Override
    public boolean isTextureInside() {
        return true;
    }

    @Override
    public void initLocation(int programe) {
        progressLocation = GLES20.glGetUniformLocation(programe, "vProgress");
        iResolutionLocation = GLES20.glGetUniformLocation(programe, "iResolution");
        showBoomLocation = GLES20.glGetUniformLocation(programe, "showBoom");
    }

    @Override
    public void drawLocation(BaseEvaluate baseEvaluate) {
        //如果不在范围内,进行显示
        if (boomEvaluate == null) {
            return;
        }
        boomEvaluate.draw();
        LogUtils.v("", getActionRender().getFrameIndex() + "," + flashCount);
        LogUtils.v("", "getCustomValue:" + boomEvaluate.getCustomValue());
        GLES20.glUniform2f(iResolutionLocation, width, height);
        if (getActionRender().getFrameIndex() < flashCount) {
            GLES20.glUniform1i(showBoomLocation, 1);
            GLES20.glUniform1f(progressLocation, boomEvaluate.getCustomValue());
        } else {
            GLES20.glUniform1i(showBoomLocation, 0);
        }
    }

}
