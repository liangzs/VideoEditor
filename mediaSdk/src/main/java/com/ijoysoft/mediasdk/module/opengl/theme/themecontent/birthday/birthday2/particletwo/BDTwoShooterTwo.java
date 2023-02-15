
package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.particletwo;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.IShooter;

import java.util.List;
import java.util.Random;

/**
 * 这是发射移动的光子，定义好首尾坐标，然后定义分布的权重，生成粒子光线
 */
public class BDTwoShooterTwo implements IShooter {
    private final Random random = new Random();

    private float[] directionVector = new float[4];
    //两点中的帧集合
    private int offsetIndex;
    private float[] offsetXs;
    private float[] offsetYs;
    private long aveDuration;
    //总帧数
    private int frameIndex;
    private Geometry.Point currentPoint;
    private int maxNum = 25;
    List<Geometry.Point> points;

    public BDTwoShooterTwo(List<Geometry.Point> points, long duration, int maxNum) {
        directionVector[0] = random.nextFloat() - 0.5f;
        directionVector[1] = random.nextFloat() - 0.5f;
        directionVector[2] = 0f;
        frameIndex = 0;
        this.maxNum = maxNum;
        this.points = points;
//        this.frameCount = (int) (duration * ConstantMediaSize.FPS / 1000);
        //计算多点之间的路线分配
        offsetXs = new float[points.size() - 1];
        offsetYs = new float[points.size() - 1];
        aveDuration = duration / (points.size() - 1);
        float[] temp;
        for (int i = 0; i < points.size() - 1; i++) {
            temp = createOffset(points.get(i), points.get(i + 1), aveDuration / 3);
            offsetXs[i] = temp[0];
            offsetYs[i] = temp[1];
        }
        currentPoint = points.get(0);
    }

    @Override
    public void addParticles(BaseParticleSystem particleSystem, float currentTime) {
        frameIndex++;
        if (frameIndex % 3 == 0) {
            offsetIndex = (int) (currentTime * 1000 / aveDuration);
            offsetIndex = offsetIndex > offsetXs.length - 1 ? offsetXs.length - 1 : offsetIndex;
            currentPoint.x = currentPoint.x + offsetXs[offsetIndex];
            currentPoint.y = currentPoint.y + offsetYs[offsetIndex];
            addParticleImpl(particleSystem, currentTime);
            addParticleImpl(particleSystem, currentTime);
        }
    }

    private float[] createOffset(Geometry.Point startPoint, Geometry.Point endPoint, long duration) {
        float translateX = endPoint.x - startPoint.x;
        float translateY = endPoint.y - startPoint.y;
        int count = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        float[] xy = new float[]{translateX / count, translateY / count};
        return xy;
    }


    private void addParticleImpl(BaseParticleSystem particleSystem, float currentTime) {
        float speedAdjustment = random.nextFloat();
        //怎么x和y值是反的
        Geometry.Vector thisDirection = new Geometry.Vector(
                (random.nextFloat() - 0.5f) * speedAdjustment * 2f,
                (random.nextFloat() - 0.5f) * speedAdjustment,
                0 * speedAdjustment);
        particleSystem.addParticle(currentPoint, 0, thisDirection, currentTime);
    }

    /**
     * 初始化粒子集合，需要关注的是生命周期，初始化时需要线性递减
     */
    public void initParticles(BDTwoSystemTwo particleSystem) {
//        frameIndex = maxNum;
        for (int i = 0; i < maxNum; i++) {
            float speedAdjustment = random.nextFloat();
            //怎么x和y值是反的
            Geometry.Vector thisDirection = new Geometry.Vector(
                    (random.nextFloat() - 0.5f) * speedAdjustment * 2f,
                    (random.nextFloat() - 0.5f) * speedAdjustment,
                    0 * speedAdjustment);
//            currentPoint.x = currentPoint.x + offsetXs[offsetIndex] * 0.5f;
//            currentPoint.y = currentPoint.y + offsetYs[offsetIndex] * 0.5f;
            particleSystem.initParticles(currentPoint, thisDirection, (i + 1) * 1.f / maxNum, i);
        }
    }
}
