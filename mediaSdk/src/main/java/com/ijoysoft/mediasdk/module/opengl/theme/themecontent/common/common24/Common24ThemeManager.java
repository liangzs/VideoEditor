package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common24;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingSystem;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager;

import java.util.Collections;
import java.util.Random;

/**
 * @author hayring
 * @date 2022/1/18  13:53
 */
public class Common24ThemeManager extends Common8ThemeManager {

    @Override
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {
        GifOriginFilter gifOriginFilter = new GifOriginFilter();
        globalizedGifOriginFilters = Collections.singletonList(gifOriginFilter);
        gifOriginFilter.setFrames(mediaItem.getDynamicMitmaps().get(0));
    }


    @Override
    protected void onGifSizeChanged(int index, int width, int height) {
        if (globalizedGifOriginFilters == null || index != 0) {
            return;
        }

        GifOriginFilter gifOriginFilter = globalizedGifOriginFilters.get(0);
        gifOriginFilter.setOnSizeChangedListener((width1, height1) -> {
            if (width >= height) {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2f);
            } else {
                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 1f);
            }
        });
        gifOriginFilter.onSizeChanged(width, height);
    }

    /**
     * 粒子位置生成，更新器
     */
    public static class ParticlePositionSystem implements MyMovingSystem.ParticleStartPositionGenerator, MyMovingSystem.ParticlePositionUpdater {



        /**
         * 标记数组
         * prev[][0]表示向左移或向右移
         * prev[][1]表示当前记录的粒子的y轴坐标
         */
        float[] prev;

        Random random = new Random();


        @Override
        public void setPosition(float[] particles, int offset) {
            //初始化prev数组
            if (prev == null) {
                prev = new float[particles.length/MyMovingSystem.TOTAL_COUNT];
            }

            //下标
            int index = offset / MyMovingSystem.TOTAL_COUNT;
            if (prev[index] == 0f) {
                //检查初始标记，如果该位置未曾有粒子，则随即在屏幕中间生成
                particles[offset] = (random.nextFloat() - 0.5f) * 2f;
                prev[index] = random.nextBoolean() ? -0.01f : 0.01f;
            } else {
                //若该位置曾经有粒子存在，则在屏幕两侧生成
                particles[offset] = random.nextBoolean() ? -1f : 1f;
                prev[index] = particles[offset] * -0.01f;
            }
            particles[offset + 1] = getPositionY(particles, offset + 1);
        }

        @Override
        public void update(float[] particle, int offset, int length) {
            particle[offset] += prev[offset / MyMovingSystem.TOTAL_COUNT];
        }

        /**
         * 以尽量间隔开的方式获取y轴随机位置
         */
        public float getPositionY(float[] particles, int offsetY) {
            float delta = 0.2f;

            float result = 0f;
            boolean continueSearch = true;
            while (continueSearch) {
                delta *= 0.5f;
                LogUtils.v(getClass().getSimpleName(), "delta: " + delta);
                result = random.nextFloat() * 1.3671875f - 1f;
                continueSearch = false;
                for (int index = offsetY % MyMovingSystem.TOTAL_COUNT; index < particles.length && !continueSearch; index += MyMovingSystem.TOTAL_COUNT) {
                    if (index == offsetY) {
                        continue;
                    }
                    //两点差大于delta说明距离隔得够开
                    continueSearch = Math.abs(particles[index] - result) < delta;
                }
            }
            return result;

        }




    }

}
