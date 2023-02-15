package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6;

import androidx.annotation.IntDef;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class CDSixSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_SIZE_COUNT + POSITION_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private float current;
    private float origin;
    private @TYPE
    int type;
    private int num;


    public CDSixSystem(@TYPE int type) {
        this.type = type;
        switch (type) {
            case ONE:
                num = 3;
                break;
            case TWO:
                num = 2;
                break;
        }
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
                current = particles[currentOffset] + 4;
                origin = particles[currentOffset + 1];
                current = current > origin ? origin : current;
                particles[currentOffset] = current;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
    }


    public void addParticles() {
        switch (type) {
            case ONE:
                addParticlesOne();
                break;
            case TWO:
                addParticlesTwo();
                break;
        }
    }

    public void addParticlesOne() {
        Geometry.Point position;
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 10;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(0.4f, 0.85f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(0.8f, 0.65f, 0f);
                    break;
                case 2:
                    position = new Geometry.Point(0.6f, 0.75f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 4;//大小
                    break;
                default:
                    position = new Geometry.Point(0, 0, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;

            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }

    public void addParticlesTwo() {
        Geometry.Point position;
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 4;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(0.85f, -0.6f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(-0.85f, 0.5f, 0f);
                    break;
                default:
                    position = new Geometry.Point(0, 0, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;

            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }


    public static final int ONE = 1;
    public static final int TWO = 2;

    @IntDef({ONE, TWO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

}
