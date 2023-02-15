package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

/**
 * gif绘制
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6.CDSixSystem}
 */
public class GifSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TOTAL_COUNT =
                    POSITION_COMPONENT_COUNT
                    + PARTICLE_POINT_SIZE_COUNT ;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private final int num;

    private final float[][] points;


    public GifSystem(float[][] points) {
        this.points = points;
        num = points.length;
        particles = new float[num * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
    }



    @Override
    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                1, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, num);
    }




    @Override
    public void addParticles() {
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            float[] point = points[i];
            //记录生命周期时间
            particles[currentOffset++] = point[3] * ConstantMediaSize.getMinWH();
            particles[currentOffset++] = point[3] * ConstantMediaSize.getMinWH();
            particles[currentOffset++] = point[0];
            particles[currentOffset++] = point[1];
            particles[currentOffset] = point[2];

            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }






}
