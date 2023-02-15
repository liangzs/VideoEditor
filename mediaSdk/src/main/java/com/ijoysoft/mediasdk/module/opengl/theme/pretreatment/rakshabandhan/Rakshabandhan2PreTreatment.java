package com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.DownloadedPreTreatmentTemplate;

import java.util.ArrayList;
import java.util.List;

public class Rakshabandhan2PreTreatment extends DownloadedPreTreatmentTemplate {


    private static final String[][] SRCS = new String[][]{
            {"/rakshabandhan2_padding1", "/rakshabandhan2_line_up", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle2"},
            {"/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower", "/rakshabandhan2_flower2", "/rakshabandhan2_flower2"},
            {"/rakshabandhan2_padding2", "/rakshabandhan2_line", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle2"},
            {"/rakshabandhan2_title", "/rakshabandhan2_circle2"},
            {"/rakshabandhan2_padding3", "/rakshabandhan2_line", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle", "/rakshabandhan2_circle2"}
    };


    @Override
    protected String[][] getSources() {
        return SRCS;
    }
}
