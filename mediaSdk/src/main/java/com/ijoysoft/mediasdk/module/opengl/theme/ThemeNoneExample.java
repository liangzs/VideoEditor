package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;

public class ThemeNoneExample extends BaseBlurThemeExample {
    public ThemeNoneExample(MediaItem mediaItem,int width,int height) {
        super((int) mediaItem.getDuration(), mediaItem.getPagDuration() != 0 ? (int) mediaItem.getPagDuration() : ConstantMediaSize.TRANSITION_DURATION,
                (int) mediaItem.getDuration() - ConstantMediaSize.TRANSITION_DURATION - DEFAULT_OUT_TIME, DEFAULT_OUT_TIME, true);
        setZView(0);
    }
}
