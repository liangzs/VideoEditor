package com.ijoysoft.mediasdk.module.opengl.transition;

import android.graphics.Matrix;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;

import org.libpag.PAGFile;

/**
 * 采用pag资源进行转场操作,为了主元素切换简便，可以挂靠一个fade的opengl的转场
 * 用于下载数据的加载
 */
public class PAGDownloadFilter extends PAGTransitionFilter {
    public PAGDownloadFilter(TransitionType transitionType) {
        super(transitionType);
        if (transitionType.getLocalPath() != null) {
            mPAGFile = PAGFile.Load(transitionType.getLocalPath());
        }
    }

    /**
     * {@link com.ijoysoft.mediasdk.common.utils.BitmapUtil#fitBitmap(String, int, int, int, boolean)}
     * 存在inside和crop两种模式，单pag有透明度的应该采取crop模式
     * <p>
     * 覆盖mediadrawer的matirx拉满铺伸模式{{@link GlobalDrawer#drawPrepare()}}
     */
    @Override
    public void pagTransitionPrepare() {
        Matrix matrix = new Matrix();
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());
        int offsetx;
        int offsety;
        float sw = (float) width / mPAGFile.width();//9
        float sh = (float) height / mPAGFile.height();//16
        if (sh > sw) {
            offsetx = (int) (mPAGFile.width() * sh - width);
            offsety = 0;
        } else {
            offsetx = 0;
            offsety = (int) (mPAGFile.height() * sw - height);
        }
        float cropScale = Math.max(sw, sh);
        matrix.postScale(cropScale, cropScale);
        matrix.postTranslate(-offsetx / 2f, -offsety / 2f);
        //放大后平移居中
        LogUtils.v("PAGLocalTransitionFilter", ":" + mPAGFile.width() + "," + mPAGFile.height());

        mPAGFile.setMatrix(matrix);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
