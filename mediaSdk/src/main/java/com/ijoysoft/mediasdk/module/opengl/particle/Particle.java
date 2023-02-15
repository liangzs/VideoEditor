package com.ijoysoft.mediasdk.module.opengl.particle;

/**
 * 兼顾x,y,z三者信息，所以用到数组
 */
public class Particle {
    private float lifeTime;
    private boolean dead;

    public Particle(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    /**
     * 每次update减少生命
     */
    public void update() {
        lifeTime -= 0.2f;
    }


    public void useLife() {
        lifeTime -= 0.03f;
    }


    public boolean checkIsDead() {
        return lifeTime < 0;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }
}
