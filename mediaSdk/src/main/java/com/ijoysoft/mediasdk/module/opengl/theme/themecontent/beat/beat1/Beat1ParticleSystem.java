package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat1;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;

import java.util.Random;

public class Beat1ParticleSystem implements IParticle.IAddParticle {
    Random random = new Random();

    @Override
    public void addParticle(float[] particles, int currentOffset, float particleStartTime, ParticleBuilder builder) {
        //记录生命周期时间
        float temp = random.nextInt(4) + 1;
        if (builder.randomLife != 0) {
            temp = random.nextFloat() * builder.randomLife + builder.minLife;
        }
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期
        float pointSize = (random.nextInt(builder.randomPointSize) + 1) * (ConstantMediaSize.getMinWH() / builder.basePointSize);
        if (builder.isScale) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = pointSize;//当前大小
        }
        //pointsize
        particles[currentOffset++] = pointSize;
        //locaion
        Geometry.Point position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -1.4f + 0.4f, 0f);
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.4f;
        particles[currentOffset++] = (random.nextFloat()) * 0.2f;
        particles[currentOffset++] = 0f;

        float[] colors = randomColor();
        /*颜色*/
        particles[currentOffset++] = colors[0];
        particles[currentOffset++] = colors[1];
        particles[currentOffset++] = colors[2];
        //time
        particles[currentOffset++] = particleStartTime;
        //纹理
        particles[currentOffset++] = randomParticle();
        particles[currentOffset++] = 1f;//alpha
    }

    /**
     * 等概率生成颜色
     * EB952C  90% 244,144,26
     * F000FF  5% 240,0,255
     * ffffff  5%
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 90) {
            result[0] = 244f / 255f;
            result[1] = 144f / 255f;
            result[2] = 26f / 255f;
        } else if (a < 95) {
            result[0] = 240f / 255f;
            result[1] = 0;
            result[2] = 1f;
        } else {
            result[0] = 1f;
            result[1] = 1f;
            result[2] = 1f;
        }
        return result;
    }

    /**
     * @return
     */
    private int randomParticle() {
        int a = random.nextInt(100);
        if (a < 70) {
            return 0;
        } else {
            return 1;
        }
    }
}
