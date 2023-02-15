package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;
import com.ijoysoft.mediasdk.view.BackgroundType;

import java.util.List;

/**
 * 挂在时间戳的预处理
 */
public abstract class BaseTimePreTreatment implements IPretreatment {
    public static final int DURATION = 1200;

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        return null;
    }

    @Override
    public long dealDuration(int index) {
        return DURATION;
    }

    @Override
    public int createDuration() {
        return DURATION;
    }

    @Override
    public int getMipmapsCount() {
        return 0;
    }

    @Override
    public Bitmap scaleBitmap(PretreatConfig pretreatConfig) {
        Bitmap bitmap = BitmapUtil.getSmallBitmapByWH(pretreatConfig.getPath(), pretreatConfig.getRotation(),
                ConstantMediaSize.localBitmapWidth, ConstantMediaSize.localBitmapHeight);
        return bitmap;

    }

    @Override
    public Bitmap fitRatioBitmap(PretreatConfig pretreatConfig) {
        return null;
    }

    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if (pretreatConfig.isVideo()) {
            return null;
        }
        return scaleBitmap(pretreatConfig);
    }

    @Override
    public TransitionFilter createTransition(int index) {
        return null;
    }

    @Override
    public int afterPreRotation() {
        return 0;
    }


    @Override
    public Bitmap addFirstFrame(Bitmap bitmap) {
        return null;
    }

    @Override
    public Bitmap addFrame(int res) {
        return null;
    }


    @Override
    public Bitmap getTempBitmap(Bitmap bitmap, int index) {
        return null;
    }


    public abstract List<Bitmap> createMimapBitmaps();


    @Override
    public List<Bitmap> getRatio11(int index) {
        return getRatio916(index);
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        return getRatio916(index);
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        return null;
    }

    @Override
    public List<Bitmap> getRatio43(int index) {
        return getRatio169(index);
    }

    @Override
    public List<Bitmap> getRatio34(int index) {
        return getRatio916(index);
    }

    @Override
    public Bitmap addText(String Text) {
        return null;
    }

    @Override
    public boolean isNeedFrame() {
        return false;
    }

    @Override
    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return null;
    }

    public boolean existRatio() {
        return false;
    }
}
