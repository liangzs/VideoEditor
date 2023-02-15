package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import org.libpag.PAGFile;

import java.util.List;

/**
 * 加边框，加时长，首张，预加载
 */
public interface IPretreatment {

    Bitmap scaleBitmap(PretreatConfig pretreatConfig);

//    Bitmap scaleBitmap(String path);

    Bitmap fitRatioBitmap(PretreatConfig pretreatConfig);

//    Bitmap addFrame(String path, int rotation, int index);

//    Bitmap addFrame(RatioType ratioType, String path, int rotation, int index, boolean blur, int colorValue);

    Bitmap addFrame(PretreatConfig pretreatConfig);

    int afterPreRotation();

    Bitmap addFirstFrame(Bitmap bitmap);

    Bitmap addFrame(int res);

    long dealDuration(int index);

    int createDuration();

    Bitmap getTempBitmap(Bitmap bitmap, int index);

    List<Bitmap> getMimapBitmaps(RatioType ratioType, int index);

    List<Bitmap> getRatio11(int index);

    List<Bitmap> getRatio169(int index);

    List<Bitmap> getRatio916(int index);

    List<Bitmap> getRatio43(int index);

    List<Bitmap> getRatio34(int index);

    Bitmap addText(String Text);

    //是否重新预处理主元素
    boolean isNeedFrame();

    int getMipmapsCount();

    /**
     * TransitionFilter createTransition(int index);
     * 获取gif 在主题文件中的位置
     *
     * @param ratioType 比例（暂时不用）
     * @return same instance!!!!!!!!!!多次调用返回同一示例！
     */
    String[][] getFinalDynamicMimapBitmapSources(RatioType ratioType);

    TransitionFilter createTransition(int index);

    default List<PAGFile> getThemePags() {
        return null;
    }

}
