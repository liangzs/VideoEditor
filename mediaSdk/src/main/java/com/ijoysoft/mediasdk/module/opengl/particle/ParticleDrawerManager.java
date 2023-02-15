package com.ijoysoft.mediasdk.module.opengl.particle;


import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个主题中，可能涉及多个粒子的形式，此类用于管理粒子
 * 此类只做实时渲染，不做离屏渲染
 */
public class ParticleDrawerManager implements IParticleRender {
    private List<IParticleRender> listParicles;

    public ParticleDrawerManager() {
        listParicles = new ArrayList<>();
    }

    public void addParticle(IParticleRender iRender) {
        listParicles.add(iRender);
    }

    @Override
    public void onDestroy() {
        for (IRender render : listParicles) {
            render.onDestroy();
        }
    }

    @Override
    public void onPlepare(int index) {
        for (IParticleRender render : listParicles) {
            render.onPlepare(index);
        }
    }

    @Override
    public void onSurfaceCreated() {
        for (IParticleRender render : listParicles) {
            render.onSurfaceCreated();
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        for (IParticleRender render : listParicles) {
            render.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        }
    }

    @Override
    public void onDrawFrame() {
        for (IParticleRender render : listParicles) {
            render.onDrawFrame();
        }
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return ObjectUtils.isEmpty(listParicles);
    }

    public List<IParticleRender> getListParicles() {
        return listParicles;
    }

    /**
     * 设置矩阵
     */
    public void setMatrix(float[] matrix) {
        for (IParticleRender render : listParicles) {
            render.setMatrix(matrix);
        }
    }


}
