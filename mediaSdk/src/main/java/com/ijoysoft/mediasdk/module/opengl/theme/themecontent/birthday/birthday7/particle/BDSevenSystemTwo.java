package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 锥子型，不停放大
 */
public class BDSevenSystemTwo extends BaseParticleSystem {
    //    private static final int PARTICLE_POINT_LIFETIME_COUNT = 1;
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TOTAL_COUNT =
            POSITION_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT;


    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private static final int starNum = 30;
    private float x;
    private float y;

    public BDSevenSystemTwo() {
        particles = new float[starNum * TOTAL_COUNT];
        maxParticleCount = starNum;
        vertexArray = new ParticleUtil(particles);
    }


    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }


    /**
     * 更新时间，更新大小
     */
    public void updateParticle(float currentTime) {
        if (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, currentTime, false);
            currentParticleCount++;
            return;
        }
        if (random.nextInt(3) < 2) {
            return;
        }
        nextParticle++;
        if (nextParticle == maxParticleCount) {
            nextParticle = 0;
        }
        addParticleImpl(nextParticle, currentTime, false);
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


    private void addParticleImpl(int index, float time, boolean init) {
        final int particleOffset = index * TOTAL_COUNT;
        Geometry.Point point = new Geometry.Point(0.6f, -0.6f, 0);
        int currentOffset = particleOffset;
        //记录生命周期时间
        particles[currentOffset++] = point.x;
        particles[currentOffset++] = point.y;
        particles[currentOffset++] = point.z;

        particles[currentOffset++] = evaluateDirection();
        particles[currentOffset++] = 0.2f;
        particles[currentOffset++] = 0;

        particles[currentOffset++] = evaluatePointSize();
        particles[currentOffset++] = time;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    private int evaluatePointSize() {
        int a = random.nextInt(100);
        int b;
        if (a < 60) {
            b = ConstantMediaSize.getMinWH() / 10;
        } else if (a < 80) {
            b = ConstantMediaSize.getMinWH() / 15;
        } else {
            b = ConstantMediaSize.getMinWH() / 20;
        }
        return b;
    }

    private float evaluateDirection() {
        int a = random.nextInt(100);
        float b;
        if (a < 50) {
            b = random.nextFloat() * 0.2f - 0.1f;
        } else if (a < 75) {
            b = -0.1f - 0.2f * random.nextFloat();
        } else {
            b = 0.1f + random.nextFloat() * 0.2f;
        }
        return b;
    }
}
