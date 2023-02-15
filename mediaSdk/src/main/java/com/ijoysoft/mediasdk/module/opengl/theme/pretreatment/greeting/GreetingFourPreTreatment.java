package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author       lfj
 * @ date         2020/10/16
 * @ description
 */
public class GreetingFourPreTreatment extends BasePreTreatment {
    private final int MIPCOUNT = 4;

    @Override
    public int getMipmapsCount() {
        return MIPCOUNT;
    }
    @Override
    public List<Bitmap> getRatio11(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme11_border_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme11_border_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme11_border_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme11_border_2" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio169(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme169_border_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme169_border_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme169_border_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme169_border_2" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio916(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme916_border_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme916_border_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme916_border_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme916_border_4" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio43(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme43_border_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme43_border_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme43_border_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme43_border_2" + SUFFIX));
                break;
        }
        return list;
    }

    @Override
    public List<Bitmap> getRatio34(int index) {
        List<Bitmap> list = new ArrayList<>();
//        index = 3;
        switch (index % MIPCOUNT) {
            case 0:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme34_border_1" + SUFFIX));
                break;
            case 1:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme34_border_2" + SUFFIX));
                break;
            case 2:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme34_border_3" + SUFFIX));
                break;
            case 3:
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_four_theme34_border_2" + SUFFIX));
                break;
        }
        return list;
    }
}
