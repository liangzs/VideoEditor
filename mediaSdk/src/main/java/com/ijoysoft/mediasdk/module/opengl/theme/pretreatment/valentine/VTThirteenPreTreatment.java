package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.PretreatConfig;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

public class VTThirteenPreTreatment extends BasePreTreatment {
    @Override
    public int afterPreRotation() {
        return 0;
    }
    @Override
    public Bitmap addFrame(PretreatConfig pretreatConfig) {
        if(pretreatConfig.isVideo()){
            return null;
        }
        Bitmap bitmap = fitRatioBitmap( pretreatConfig);
        int num = FRAME_BORDER / 2;
        int width = bitmap.getWidth() + num / 2;
        int height = bitmap.getHeight() + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        String res = ConstantMediaSize.themePath + "/vt_thirteen_theme_border1" + SUFFIX;

        switch (pretreatConfig.getIndex() % 5) {
            case 0:
            case 2:
            case 4:
                res = ConstantMediaSize.themePath + "/vt_thirteen_theme_border1" + SUFFIX;
                break;
            case 1:
            case 3:
                res = ConstantMediaSize.themePath + "/vt_thirteen_theme_border2" + SUFFIX;
                break;
        }
        Bitmap bit2 = BitmapUtil.decriptImage(res);
        if(pretreatConfig.getRotation()!=0){
            Matrix m = new Matrix();
            m.postRotate(pretreatConfig.getRotation());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        canvas.drawBitmap(bitmap, num / 4, num / 2, null);
        bit2 = Bitmap.createScaledBitmap(bit2, width, height, true);
        canvas.drawBitmap(bit2, 0, 0, null);
        bitmap.recycle();
        bit2.recycle();
        return newBitmap;
    }


    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
        Resources resources = MediaSdk.getInstance().getResouce();
//        index = 4;
        switch (index % 5) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme1_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme2_1" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme3_1" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme2_1" + SUFFIX));
                break;
            case 4:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme5_1" + SUFFIX));
                break;
        }
        return list;
    }
}
