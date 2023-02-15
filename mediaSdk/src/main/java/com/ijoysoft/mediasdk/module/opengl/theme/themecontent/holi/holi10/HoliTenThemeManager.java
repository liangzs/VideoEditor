package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

public class HoliTenThemeManager extends ThemeOpenglManager {
    private ImageOriginFilter bgImageFilter;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public HoliTenThemeManager() {
        bgImageFilter = new ImageOriginFilter();
    }
    @Override
    public TransitionType themeTransition() {
        return TransitionType.READ_BOOK;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
//        index = 4;
        switch (index % 5) {
            case 0:
                actionRender = new HoliTenThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliTenThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new HoliTenThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new HoliTenThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new HoliTenThemeFive((int) mediaItem.getDuration(), true, width, height);
                break;
        }
        return actionRender;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        bgImageFilter.create();
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        bgImageFilter.draw();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        bgImageFilter.onSizeChanged(width, height);

        Bitmap bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_ten_border");
        bgImageFilter.setBackgroundTexture(bitmap);
        bgImageFilter.setVertex(adjustScalingFixX(width, height, bitmap.getWidth(), bitmap.getHeight()));
    }

    private float[] adjustScalingFixX(int showWidth, int showHeight, int framewidth, int frameheight) {
        float ratio1 = (float) showWidth / framewidth;
        // 居中后图片显示的大小
        int imageHeightNew = Math.round(frameheight * ratio1);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioHeight = 2f * imageHeightNew / showHeight;//-1到1所以为2
        float[] cube = new float[]{-1f, ratioHeight - 1f, -1f, -1f,
                1f, ratioHeight - 1, +1f, -1f};
        return cube;
    }

}
