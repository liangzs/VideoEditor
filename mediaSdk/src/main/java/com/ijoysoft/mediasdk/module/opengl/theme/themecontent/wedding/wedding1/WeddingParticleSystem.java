package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class WeddingParticleSystem extends BaseParticleSystem {
    private static int POINTSIZE_COMPOMENT_COUNT = 1;
    protected static int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + POINTSIZE_COMPOMENT_COUNT;

    protected static int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;


    public WeddingParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
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
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += POINTSIZE_COMPOMENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                POINTSIZE_COMPOMENT_COUNT, STRIDE);

    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction,
                            float particleStartTime) {
        position = new Geometry.Point(random.nextFloat() * -2f + 1f, 0.3f * random.nextFloat() - 1f, 0f);
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;

        int currentOffset = particleOffset;
        nextParticle++;

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }

        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0;
        }

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;
        particles[currentOffset++] = 1.f * random.nextInt(256) / 255f;
        particles[currentOffset++] = 1.f * random.nextInt(256) / 255f;
        particles[currentOffset++] = 1.f * random.nextInt(256) / 255f;
        float offset = random.nextInt(2) == 0 ? -1f : 1f;
        particles[currentOffset++] = 0.05f * offset;
        particles[currentOffset++] = 0.3f;
        particles[currentOffset++] = 0f;

        particles[currentOffset++] = particleStartTime;
        particles[currentOffset++] = evaluatePointSize();

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    /**
     * 设置最大圈的概率是10，其余两个分别是40,
     */
    private int evaluatePointSize() {
        int a = random.nextInt(100);
        int b;
        if (a < 45) {
            b = 1;
        } else if (a < 90) {
            b = 2;
        } else {
            b = 3;
        }
        return b * ConstantMediaSize.getMinWH() / 50;
    }


}
