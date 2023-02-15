package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.WedTextMoveEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WeddingTextStayAddRotate;

/**
 * 左右弹簧拉伸，旋转
 */
public class WeddingTextThree extends BaseThemeExample {

    public WeddingTextThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 1600, DEFAULT_OUT_TIME);
        enterAnimation = new WedTextMoveEvaluate(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setScale(0.1f, 1f).setWidthHeight(width, height));

        stayAction = new WeddingTextStayAddRotate(1600);
        //旋转是有加速效果的，所以需要对旋转重赋值
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME)
                .setFade(1f, 0.0f).build();
    }

    @Override
    public void drawFrame(int durationPostion) {
        super.drawFrame(durationPostion);
    }

    @Override
    protected void NoneStayAction() {
        super.NoneStayAction();
        transitionOM = MatrixUtils.getOriginalMatrix();
        Matrix.translateM(transitionOM, 0, 0, 0, -3f);
    }

    /**
     * 只重写这个方法，onCreate和onsurfacechange走父类
     *
     * @param bitmap
     */
    @Override
    public void initTexture(Bitmap bitmap) {
        super.initTexture(bitmap);
        float pos[] = {
                -1.0f, 1.1f,
                -1.0f, 0.8f,
                1.0f, 1.1f,
                1.0f, 0.8f,
        };
        setVertex(pos);
        ((WedTextMoveEvaluate) enterAnimation).setRatateCenter(0, 0.95f);
        outAnimation.setIsSubAreaWidget(true, 0, 0.95f);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
//        initTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.theme_wedding_text_one));
    }
}
