package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class BDThreeSystemTwo extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            POSITION_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT+PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    public BDThreeSystemTwo(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, maxParticleCount);
    }

    /**
     * 初始化粒子
     */
    public void addParticles() {
        Geometry.Point position;
        for (int i = 0; i < maxParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            switch (i) {
                case 0:
                    position = new Geometry.Point(-0.8f, 0.8f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(0.8f, 0.6f, 0f);
                    break;
                case 2:
                    position = new Geometry.Point(0.9f, 0.2f, 0f);
                    break;
                default:
                    position = new Geometry.Point(-0.8f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            particles[currentOffset++] = 0;
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 10;//大小
            particles[currentOffset] = i;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }
}
