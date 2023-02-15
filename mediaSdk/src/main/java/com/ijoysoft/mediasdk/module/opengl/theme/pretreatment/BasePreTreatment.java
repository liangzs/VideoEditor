package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.FileUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.view.BackgroundType;

import java.util.List;

public class BasePreTreatment implements IPretreatment {
    public static final int FRAME_BORDER = 30;
    protected RatioType mRatioType = RatioType._9_16;
    public String SUFFIX = "";
    //主题1 等

    public final int THEME_ONE_DURATION = 4200;
    //主题2、gl转场等

    public final int THEME_TWO_DURATION = 4900;
    //波浪主题，主题2等

    public final int THEME_THREE_DURATION = 5400;

    private final int DEFAULTCOUNT = 5;

    @Override
    public int getMipmapsCount() {
        return DEFAULTCOUNT;
    }


    @Override

    public TransitionFilter createTransition(int index) {
        return null;
    }

    @Override

    public String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType) {
        return null;
    }

    public Bitmap scaleBitmap(PretreatConfig pretreatConfig) {
        //针对部分手机，如果文件是gif，则取gif的第二帧
        if (pretreatConfig.getPath().endsWith(".gif")) {
            return BitmapUtil.getGifByGlide(MediaSdk.getInstance().getContext(), pretreatConfig.getPath());
        }
        //如果文件保护存在，则给一个默认图
        if (!FileUtils.checkFileExist(pretreatConfig.getPath())) {

            return PhotoMediaItem.getDefaultBitmap();
        }
        Bitmap bitmap = BitmapUtil.getSmallBitmapByWH(pretreatConfig.getPath(), pretreatConfig.getRotation(),
                ConstantMediaSize.localBitmapWidth, ConstantMediaSize.localBitmapHeight);
        return bitmap;
    }

    public Bitmap fitAndCutRatioBitmap(PretreatConfig pretreatConfig) {

        int rotation = pretreatConfig.getRotation();
        int[] WH = ConstantMediaSize._9_16;
        switch (pretreatConfig.getRatioType()) {
            case _1_1:
                WH = ConstantMediaSize._1_1;
                break;
            case _3_4:
                WH = ConstantMediaSize._3_4;
                break;
            case _4_3:
                WH = ConstantMediaSize._4_3;
                break;
            case _9_16:
                WH = ConstantMediaSize._9_16;
                break;
            case _16_9:
                WH = ConstantMediaSize._16_9;
                break;
        }
        //inside显示
        return BitmapUtil.fitBitmap(pretreatConfig.getPath(), rotation, WH[0], WH[1], false);
    }

    @Override
    public Bitmap fitRatioBitmap(PretreatConfig pretreatConfig) {
        int rotation = pretreatConfig.getRotation();
        int[] WH = ConstantMediaSize._9_16;
        switch (pretreatConfig.getRatioType()) {
            case _1_1:
                WH = ConstantMediaSize._1_1;
                break;
            case _3_4:
                WH = ConstantMediaSize._3_4;
                break;
            case _4_3:
                WH = ConstantMediaSize._4_3;
                break;
            case _9_16:
                WH = ConstantMediaSize._9_16;
                break;
            case _16_9:
                WH = ConstantMediaSize._16_9;
                break;
        }
        Bitmap newBitmap = Bitmap.createBitmap(WH[0], WH[1], Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        Bitmap cropBg;
        int width;
        int height;
        int offsetX;
        int offsetY;
        switch (pretreatConfig.getBackgroundType()) {
            case BackgroundType.SELF:
                if (pretreatConfig.getBackgroundType() == BackgroundType.SELF) {
                    cropBg = BitmapUtil.fitBitmap(pretreatConfig.getPath(), rotation, WH[0], WH[1], true);
                } else {
                    cropBg = BitmapUtil.fitBitmap(pretreatConfig.getPath(), rotation, WH[0], WH[1], true);
                }
                if (cropBg == null) {
                    return null;
                }
                Bitmap blur = BitmapUtil.blurBitmap(MediaSdk.getInstance().getContext(), pretreatConfig.getBlurLevel() / 4, cropBg);
                width = cropBg.getWidth();
                height = cropBg.getHeight();
                offsetX = (WH[0] - width) / 2;
                offsetY = (WH[1] - height) / 2;
                LogUtils.i("", "offsetX:" + offsetX + ",offsetY" + offsetY);
                canvas.drawBitmap(blur, offsetX, offsetY, null);
                cropBg.recycle();
                blur.recycle();
                break;
            case BackgroundType.IMAGE:
                cropBg = BitmapUtil.fitBitmap(pretreatConfig.getCustomPath(), 0, WH[0], WH[1], true);
                canvas.drawBitmap(cropBg, 0, 0, null);
                break;
            case BackgroundType.COLOR:
            case BackgroundType.NONE:
                canvas.drawColor(pretreatConfig.getColorValue());
                break;
        }
        //inside显示
        cropBg = BitmapUtil.fitBitmap(pretreatConfig.getPath(), rotation, WH[0], WH[1], false);
        width = cropBg.getWidth();
        height = cropBg.getHeight();
        offsetX = (WH[0] - width) / 2;
        offsetY = (WH[1] - height) / 2;
        LogUtils.i("", "cropBg:" + cropBg.getWidth() + "," + cropBg.getHeight() + "offsetX" + offsetX + ",offsetY" + offsetY);
        canvas.drawBitmap(cropBg, offsetX, offsetY, null);
        cropBg.recycle();
        return newBitmap;
    }


    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if (pretreatConfig.isVideo()) {
            return null;
        }
        return scaleBitmap(pretreatConfig);
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
    public long dealDuration(int index) {
        long originDuration = createDuration();
        long tempDuration;
        switch (ConstantMediaSize.themeType) {
            case WEDDING_FOUR:
            case TRAVEL_NINE:
            case BIRTHDAY_SEVEN://没有相交结果
            case BIRTHDAY_NINETEEN:
                tempDuration = originDuration;
                break;
            case NONE:
                tempDuration = 0;
                break;
            default:
                tempDuration = originDuration - ConstantMediaSize.themeType.getEndOffset();
                if (index == 0 && !(ThemeHelper.createThemeManager() instanceof ThemeOpenglManager)) {
                    tempDuration = originDuration;
                }
                //合成的时候再进行控制最后一张的出场取消
//              tempDuration = last ? tempDuration - ConstantMediaSize.END_OFFSET : tempDuration;
                break;
        }
        return tempDuration;
    }

    @Override
    public int createDuration() {
        return ConstantMediaSize.themeType.getThemeTimeType();
    }

    @Override
    public Bitmap getTempBitmap(Bitmap bitmap, int index) {
        return null;
    }

    @Override
    public List<Bitmap> getMimapBitmaps(RatioType ratioType, int index) {
        this.mRatioType = ratioType;
        switch (ratioType) {
            case _1_1:
                return getRatio11(index);
            case _3_4:
                return getRatio34(index);
            case _4_3:
                return getRatio43(index);
            case _9_16:
                return getRatio916(index);
            case _16_9:
                return getRatio169(index);
            case NONE:
                return getRatio11(index);
        }
        return getRatio916(index);
    }

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


}
