
package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;


import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.IShooter;
import com.ijoysoft.mediasdk.module.opengl.particle.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WeddingParticleShooter implements IShooter {
    private final Geometry.Point position;
    private final int color;

    private final float angleVariance;
    private final float speedVariance;

    private final Random random = new Random();
    private List<Particle> particleList;//定义10个例子，用for循环也不算慢
    private int maxNum;

    public WeddingParticleShooter(
            Geometry.Point position, int color,
            float angleVarianceInDegrees, float speedVariance, int maxNum) {
        this.position = position;
        this.color = color;
        this.angleVariance = angleVarianceInDegrees;
        this.speedVariance = speedVariance;

        this.maxNum = maxNum;
        particleList = new ArrayList<>();
    }

    @Override
    public void addParticles(BaseParticleSystem particleSystem, float currentTime) {
        if (particleList.size() < maxNum) {
            addParticleImpl(particleSystem, currentTime);
            particleList.add(new Particle(random.nextInt(4) + 1));
            return;
        }
        for (Particle particle : particleList) {
            particle.useLife();
            if (particle.checkIsDead()) {
                addParticleImpl(particleSystem, currentTime);
                particle.setLifeTime(random.nextInt(4) + 1);
            }
        }
    }

    private void addParticleImpl(BaseParticleSystem particleSystem, float currentTime) {
        particleSystem.addParticle(position, color, null, currentTime);
    }
}
