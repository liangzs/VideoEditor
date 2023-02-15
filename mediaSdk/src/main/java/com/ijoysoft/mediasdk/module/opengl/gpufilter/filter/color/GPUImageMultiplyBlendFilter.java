package com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.color;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

public class GPUImageMultiplyBlendFilter extends GPUImageTwoInputFilter {
    public static final String MULTIPLY_BLEND_FRAGMENT_SHADER = "precision mediump float;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2;\n" +
            "\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " uniform float strength;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     float resultAlpha=base.a*(1.0/(base.a+0.00001));\n" +
            "     lowp vec4 overlayer = texture2D(inputImageTexture2, textureCoordinate2);\n" +
            "          \n" +
            "     gl_FragColor = mix(base,overlayer * base + overlayer * (1.0 - base.a) + base * (1.0 - overlayer.a)+0.12*strength,strength)*resultAlpha;\n" +
            " }";

    public GPUImageMultiplyBlendFilter(MagicFilterType magicFilterType, int drawableId, int alpha) {
        super(magicFilterType, MULTIPLY_BLEND_FRAGMENT_SHADER);
        Drawable drawable = ContextCompat.getDrawable(MediaSdk.getInstance().getContext(), drawableId);
        drawable.setBounds(0, 0, 300, 400);
//        drawable.setAlpha(alpha);
        Bitmap bitmap = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        setBitmap(bitmap);
    }
}
