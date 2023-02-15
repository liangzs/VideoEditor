package com.ijoysoft.mediasdk.module.opengl.particle;

/**
 * 注入 具体实现
 * TOTAL_COUNT =
 * LIFETIME_COMPOMENT_COUNT +
 * POSITION_COMPONENT_COUNT
 * + VECTOR_COMPONENT_COUNT
 * + COLOR_COMPONENT_COUNT
 * + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT;
 */

public class IParticle {

    /**
     * 粒子总接口
     */
    public interface IAddParticle {
        void addParticle(float[] particle, int particleOffset, float currentTime, ParticleBuilder builder);
    }

    /**
     * 创建color
     */
    public interface ICreateColor {
        float[] createColor();
    }

    /**
     * 创建索引颜色集合
     */
    public interface ICreateColorMap {
        float[] createColor();

        float[] getColor(int index, int currentFrame);
    }

    /**
     * 创建方向
     */
    public interface ICreateDirector {
        float[] createDirector();
    }

    /**
     * 创建初始位置
     */
    public interface ICreateLocation {
        float[] createLocation(int index);
    }


    /**
     * 自定义不规则的大小
     */
    public interface ICreatePointSize {
        float createPointSize();
    }

    /**
     * 重力加速度值，可根据正负值改变加速方向
     */
    public interface ICreateAccelerate {
        float createAccelerate();
    }

    /**
     * 自定义纹理设置,因为有重复照片，所以需要重复利用纹理
     */
    public interface ICreateTexture {
        int createTexture(int index);
    }

    /**
     *对象是否为空来判断是否手动设置，因为存在粒子系统的默认值不一致
     */
    public interface ITextureFlip {
        boolean isFLip();
    }
}


