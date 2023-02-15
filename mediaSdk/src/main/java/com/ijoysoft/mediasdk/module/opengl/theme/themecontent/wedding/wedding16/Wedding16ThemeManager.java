package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding16;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.SameActionOpenglThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;

/**
 * @author hayring
 * @date 2021/12/17  16:18
 */
public class Wedding16ThemeManager extends SameActionOpenglThemeManager {
    public Wedding16ThemeManager() {
        super(Wedding16Action.class);
    }

    Bitmap cloud;

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        super.drawPrepare(mediaDrawer, mediaItem, index);
        if (repeatAroundFilter == null) {
            //频谱
            repeatAroundFilter = new RepeatAroundFilter();
            cloud =  BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_cloud" + SUFFIX);
            repeatAroundFilter.init(cloud, width, height, AnimateInfo.ORIENTATION.BOTTOM);
            repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM);
        }
    }


    RepeatAroundFilter repeatAroundFilter = null;


    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        if (repeatAroundFilter != null) {
            repeatAroundFilter.draw();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (repeatAroundFilter != null) {
            repeatAroundFilter.onDestroy();
            repeatAroundFilter = null;
            cloud.recycle();
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        if (repeatAroundFilter != null) {
            repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM);
        }
    }
}
