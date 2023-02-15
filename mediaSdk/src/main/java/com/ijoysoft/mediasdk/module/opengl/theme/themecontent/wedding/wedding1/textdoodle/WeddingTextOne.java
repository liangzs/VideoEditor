package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.WeddingTextStayAnimation;

/**
 * 左右弹簧拉伸，旋转
 */
public class WeddingTextOne extends BaseThemeExample {

    public WeddingTextOne(int totalTime) {
        super(totalTime, DEFAULT_ENTER_TIME, 3300, 500);
        stayAction = new WeddingTextStayAnimation(3300, 4, 0.2f, 1f);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 2.0f, 0, 0).
                setScale(0.1f, 1f).build();
        outAnimation = new AnimationBuilder(500).setCoordinate(0, 0, 2f, 0).build();
    }


    @Override
    protected void wedThemeSpecialDeal() {
        super.wedThemeSpecialDeal();
//        Matrix.translateM(mViewMatrix, 0, 0f, -0.4f, 0f);
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
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
//        initTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.theme_wedding_text_one));
    }
}
