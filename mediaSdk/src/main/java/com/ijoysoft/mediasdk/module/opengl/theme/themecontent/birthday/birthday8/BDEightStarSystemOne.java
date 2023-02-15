package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday8;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class BDEightStarSystemOne extends BaseParticleSystem {
    private static final int PARTICLE_POINT_LIFETIME_COUNT = 1;
    private static final int PARTICLE_POINT_ALPHA_COUNT = 1;
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_LIFETIME_COUNT + PARTICLE_POINT_ALPHA_COUNT + POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT;


    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private static final int starNum = 20;
    private float x;
    private float y;

    public BDEightStarSystemOne() {
        particles = new float[starNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
    }


    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = PARTICLE_POINT_LIFETIME_COUNT;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_POINT_ALPHA_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_ALPHA_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        updateParticle();
        glDrawArrays(GL_POINTS, 0, starNum);
    }

    private float currentLife;

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        for (int i = 0; i < starNum; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, false);
                continue;
            }
            updateParticle(i);
        }
    }

    /**
     * 更新生命周期，更新透明度
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        particles[currentOffset] = particles[currentOffset] - 0.03f;
        if (particles[currentOffset] < 0.3) {//准备消失
            particles[currentOffset + 1] = particles[currentOffset + 1] - 0.1f;
        } else {
            if (particles[currentOffset + 1] < 1) {
                particles[currentOffset + 1] = particles[currentOffset + 1] + 0.1f;
                particles[currentOffset + 1] = particles[currentOffset + 1] > 1f ? 1f : particles[currentOffset + 1];
            }
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 创建粒子，v字型散布
     */
    public void addParticles() {
        for (int i = 0; i < starNum; i++) {
            addParticleImpl(i, true);
        }
    }


    private void addParticleImpl(int index, boolean isInit) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        //记录生命周期时间
        particles[currentOffset++] = random.nextInt(3) + 1.5f;//时长
        if (isInit) {
            particles[currentOffset++] = random.nextFloat();//透明度
        } else {
            particles[currentOffset++] = 0;//透明度
        }
        x = -2 * random.nextFloat() + 1;//-1到1
        y = x < 0 ? x + 0.5f : -x + 0.5f;
        x = (-0.2f * random.nextFloat() + 0.1f) + x;
        y = (-0.2f * random.nextFloat() + 0.1f) + y;
        particles[currentOffset++] = x;
        particles[currentOffset++] = y;
        particles[currentOffset++] = 0;
        particles[currentOffset++] = evaluatePointSize();
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    private int evaluatePointSize() {
        int a = random.nextInt(100);
        int b;
        if (a < 60) {
            b = ConstantMediaSize.getMinWH() / 10;
        } else if (a < 80) {
            b = ConstantMediaSize.getMinWH() / 3;
        } else {
            b = ConstantMediaSize.getMinWH() / 6;
        }
        return b;
    }
}
