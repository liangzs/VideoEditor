package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.WedTextMoveEvaluate;

/**
 * 左右弹簧拉伸，旋转
 */
public class WeddingTextTwo extends BaseThemeExample {

    public WeddingTextTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3600, DEFAULT_OUT_TIME);
//        stayAction = new WeddingTextStayAnimation(3000, 4, 0.2f, 1f);
        enterAnimation = new WedTextMoveEvaluate(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setConors(AnimateInfo.ROTATION.Z_ANXIS_RE, 180, 360).
                setScale(0.1f, 1f).setWidthHeight(width, height));
        //旋转是有加速效果的，所以需要对旋转重赋值
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME)
                .setScale(1f, 0.0f).build();



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
