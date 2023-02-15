package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class BDSevenSystemOne extends BaseParticleSystem {
    private static final int LIFETIME_COMPOMENT_COUNT = 1;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int ALPHA_COMPOMENT_COUNT = 1;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    ALPHA_COMPOMENT_COUNT +
                    POSITION_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    public BDSevenSystemOne(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = LIFETIME_COMPOMENT_COUNT;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                ALPHA_COMPOMENT_COUNT, STRIDE);
        dataOffset += ALPHA_COMPOMENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);

    }


    private float currentLife;

    public void addParticle(float particleStartTime) {
        if (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, particleStartTime, true);
            currentParticleCount++;
            return;
        }
        for (int i = 0; i < maxParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime, false);
                continue;
            }
            if (currentLife < 0.3) {
                particles[currentOffset + 1] = particles[currentOffset + 1] - 0.1f;
            }
            particles[currentOffset] = currentLife;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }


    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime, boolean isInit) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        Geometry.Point position = new Geometry.Point(random.nextFloat() * -2f + 1f, -1f - 0.4f * random.nextFloat(), 0f);
        if (isInit) {
            position = new Geometry.Point(random.nextFloat() * -2f + 1f, -1f - 1f * random.nextFloat(), 0f);

        }
        //记录生命周期时间
        particles[currentOffset++] = random.nextInt(6) + 2;
        particles[currentOffset++] = 1f;//alpha
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        particles[currentOffset++] = random.nextFloat() * 0.2f - 0.1f;
        particles[currentOffset++] = 0.2f;
        particles[currentOffset++] = 0;

        particles[currentOffset++] = particleStartTime;
        particles[currentOffset++] = evaluatePointSize();//大小
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    /**
     * 设置最大圈的概率是10，其余两个分别是40,
     */
    private int evaluatePointSize() {
        int a = random.nextInt(100);
        int b;
        if (a < 60) {
            b = ConstantMediaSize.getMinWH() / 16;
        } else if (a < 90) {
            b = ConstantMediaSize.getMinWH() / 12;
        } else {
            b = ConstantMediaSize.getMinWH() / 8;
        }

        return b;
    }

}
