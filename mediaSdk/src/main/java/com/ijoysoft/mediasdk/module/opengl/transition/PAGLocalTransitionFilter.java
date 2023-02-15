package com.ijoysoft.mediasdk.module.opengl.transition;

import android.graphics.Matrix;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;

import org.libpag.PAGFile;

/**
 * 采用pag资源进行转场操作,为了主元素切换简便，可以挂靠一个fade的opengl的转场
 */
public class PAGLocalTransitionFilter extends PAGTransitionFilter {

    public PAGLocalTransitionFilter(TransitionType transitionType) {
        super(transitionType);
        if (transitionType.getLocalPath() != null) {
            mPAGFile = PAGFile.Load(MediaSdk.getInstance().getResouce().getAssets(), transitionType.getLocalPath());
        }
    }

    /**
     * {@link com.ijoysoft.mediasdk.common.utils.BitmapUtil#fitBitmap(String, int, int, int, boolean)}
     * 存在inside和crop两种模式，单pag有透明度的应该采取crop模式
     * <p>
     * 覆盖mediadrawer的matirx拉满铺伸模式{{@link GlobalDrawer#drawPrepare(MediaItem, int)}}
     */
    @Override
    public void pagTransitionPrepare() {
        Matrix matrix = cropCenter();
        mPAGFile.setMatrix(matrix);
    }

    /**
     * 如果pag素材的宽高比例对不上的话，那么就做裁剪动作
     * 通过matrix取最大的缩放倍数
     *
     * @return
     */
    private Matrix pagtransitionMaxtrix(PAGFile pagImage, int viewWidth, int viewHeight) {
        int width = pagImage.width();
        int height = pagImage.height();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / (float) viewWidth, (float) height / (float) viewHeight);
        return matrix;
    }


}
