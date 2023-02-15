package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

/**
 * 放大旋转粒子
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6.CDSixSystem}
 */
public class RotateImageSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_SIZE_COUNT + POSITION_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private float current;
    private float origin;
    private int num;

    private float[][] points;

    private float scaleSpeed = 6f;


    public RotateImageSystem(float[][] points) {
        this.points = points;
        num = points.length;
        particles = new float[num * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
    }

    public RotateImageSystem(float[][] points, float scaleSpeed) {
        this.points = points;
        this.scaleSpeed = scaleSpeed;
        num = points.length;
        particles = new float[num * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                1, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;

    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        updateParticle();
        glDrawArrays(GL_POINTS, 0, num);
    }

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            if (particles[currentOffset] < particles[currentOffset + 1]) {
                current = particles[currentOffset] + scaleSpeed;
                origin = particles[currentOffset + 1];
                current = current > origin ? origin : current;
                particles[currentOffset] = current;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
    }


    public void addParticles() {
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            float[] point = points[i];
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = point[3] * ConstantMediaSize.getMinWH();
//            LogUtils.w(getClass().getSimpleName(), "Min WH: " + ConstantMediaSize.getMinWH());
//            LogUtils.w(getClass().getSimpleName(), "Point radius: " + point[3]);
            //TODO
            particles[currentOffset++] = point[0];
            particles[currentOffset++] = point[1];
            particles[currentOffset++] = point[2];

            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }






}
