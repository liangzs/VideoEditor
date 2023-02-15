package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.particletwo;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 暂做25个例子，然后1s的生命周期(即25f的生命周期)
 */
public class BDTwoSystemTwo extends BaseParticleSystem {
    private static final int POINT_SIZE_COUNT = 1;
    private static final int POINT_ALPHA_COUNT = 1;
    private static final int LIFE_TIME_COMPOMENT_COUNT = 1;
    private static final int TOTAL_COUNT = LIFE_TIME_COMPOMENT_COUNT +
            POSITION_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT
            + PARTICLE_START_TIME_COMPONENT_COUNT + POINT_SIZE_COUNT + POINT_ALPHA_COUNT;
    private static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private static float frameValue = 1f / 25f;

    public BDTwoSystemTwo(int maxParticleCount) {
        super(maxParticleCount);
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = LIFE_TIME_COMPOMENT_COUNT;
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
                POINT_SIZE_COUNT, STRIDE);
        dataOffset += POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                POINT_ALPHA_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        updateParticle();
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction,
                            float particleStartTime) {
        for (int i = 0; i < maxParticleCount; i++) {
            int particleOffset = i * TOTAL_COUNT;
            particles[particleOffset] = particles[particleOffset] - frameValue;
            if (particles[particleOffset] < 0) {
                addParticleImpl(position, direction, particleStartTime, 1f, i);
            }
        }
    }

    /**
     * 初始化粒子集合，需要关注的是其生命周期需要递减
     */
    public void initParticles(Geometry.Point position, Geometry.Vector direction, float life, int index) {
        final int particleOffset = index * TOTAL_COUNT;
        particles[particleOffset] = life;
        addParticleImpl(position, direction, 0, life, index);
    }

    /**
     * 添加粒子
     */
    private void addParticleImpl(Geometry.Point position, Geometry.Vector direction,
                                 float particleStartTime, float life, int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }
        particles[currentOffset++] = life;
        particles[currentOffset++] = position.x + random.nextFloat() * 0.1f;
        particles[currentOffset++] = position.y + random.nextFloat() * 0.1f;
        particles[currentOffset++] = position.z;
        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;
        particles[currentOffset++] = particleStartTime;
        particles[currentOffset++] = ConstantMediaSize.getMinWH() /4;
        particles[currentOffset++] = 1.0f;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

}
