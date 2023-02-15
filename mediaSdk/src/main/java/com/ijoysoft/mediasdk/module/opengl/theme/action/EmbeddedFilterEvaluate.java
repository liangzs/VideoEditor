package com.ijoysoft.mediasdk.module.opengl.theme.action;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EmbeddedFilter;

/**
 * @author hayring
 * @date 2022/1/5  14:18
 */
public class EmbeddedFilterEvaluate extends TimeThemeEvaluate {

    float progress;

    float progressOffset;

    float[] progressFrame;

    public EmbeddedFilterEvaluate(AnimationBuilder builder) {
        super(builder);
        if (moveAction == null) {
            progress = 0f;
            progressOffset = 1f / frameCount;
        } else {
            progressFrame = moveAction.action(duration, 0, 1);
        }

    }

    private EmbeddedFilter embeddedFilter = null;

    public EmbeddedFilter getEmbeddedFilter() {
        return embeddedFilter;
    }


    public void setEmbeddedFilter(EmbeddedFilter embeddedFilter) {
        this.embeddedFilter = embeddedFilter;
    }


    int ms = 0;


    public void drawEmbeddedFilter(int textureId) {
        drawBefore();
        embeddedFilter.setBaseScale(transitionScale);
        if (progressFrame == null) {
            LogUtils.v(embeddedFilter.getClass().getSimpleName(), "progress:" + progress);

            embeddedFilter.setProgress(progress);
            progress += progressOffset;
        } else {
            embeddedFilter.setProgress(progressFrame[frameIndex]);
        }
        embeddedFilter.drawTexture(textureId);

        frameIndex++;
    }

    @Override
    public void reset() {
        super.reset();
        progress = 0f;
    }


}
