package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine1.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class VTOneSystem extends BaseParticleSystem {
    private static final int LIFETIME_COMPOMENT_COUNT = 2;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    public VTOneSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = LIFETIME_COMPOMENT_COUNT;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                1, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

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

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
    }


    private float currentLife;

    public void addParticle(float particleStartTime) {
        if (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, particleStartTime);
            currentParticleCount++;
            return;
        }
        for (int i = 0; i < maxParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime);
                continue;
            }
            updateParticle(i);
        }
    }

    private float current;
    private float origin;

    /**
     * 更新时间，更新大小
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset] = current - 0.03f;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        Geometry.Point position = new Geometry.Point(random.nextFloat() * -2f + 1f, -0.2f * random.nextFloat() - 1f, 0f);
        //记录生命周期时间
        float temp = random.nextInt(4) + 2;
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期

        temp = (1 + random.nextInt(2)) * ConstantMediaSize.getMinWH() / 45;//(20,25,30)
        particles[currentOffset++] = temp;//当前大小
        particles[currentOffset++] = temp;//大小

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.2f;
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.2f;
        particles[currentOffset++] = 0f;

        particles[currentOffset++] = particleStartTime;
        particles[currentOffset++] = random.nextInt(4);
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


}
