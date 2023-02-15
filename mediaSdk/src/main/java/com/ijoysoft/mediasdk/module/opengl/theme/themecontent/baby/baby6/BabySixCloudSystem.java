package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby6;

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

public class BabySixCloudSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TOTAL_COUNT = PARTICLE_ALPHA_COUNT +
            PARTICLE_POINT_SIZE_COUNT + POSITION_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT
            + PARTICLE_START_TIME_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private @CLOUD
    int type;
    private int num = 0;

    public BabySixCloudSystem(@CLOUD int type) {
        this.type = type;
        switch (type) {
            case ONE:
            case THREE:
            case FIVE:
                num = 6;
                break;
            case TWO:
            case FOUR:
                num = 5;
                break;
        }
        particles = new float[num * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
    }


    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                1, STRIDE);
        dataOffset += PARTICLE_ALPHA_COUNT;

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
            if (particles[currentOffset] < 1f) {
                particles[currentOffset] = particles[currentOffset] + 0.02f > 1f ? 1 : particles[currentOffset] + particles[currentOffset] + 0.02f;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
    }

    /**
     * 新增粒子
     * float end[] = {
     * -1.0f, 1f,
     * -1.0f, 0.6f,
     * -0.3f, 1f,
     * -0.3f, 0.6f,
     * };
     */
    public void addParticles() {
        switch (type) {
            case ONE:
                addParticlesOne();
                break;
            case TWO:
                addParticlesTwo();
                break;
            case THREE:
                addParticlesThree();
                break;
            case FOUR:
                addParticlesFour();
                break;
            case FIVE:
                addParticlesFive();
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
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 6;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(-0.6f, -0.8f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 1:
                    position = new Geometry.Point(-0, -0.8f, 0f);
                    break;
                case 2:
                    position = new Geometry.Point(0.55f, -0.5f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 3:
                    position = new Geometry.Point(-0.5f, -0.2f, 0f);

                    break;
                case 4:
                    position = new Geometry.Point(-0.6f, 0.2f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 5;//大小
                    break;
                case 5:
                    position = new Geometry.Point(0.65f, 0.7f, 0f);
                    break;
                default:
                    position = new Geometry.Point(-0.6f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            //方向随机,并且在glsl中取消重力加速度
            particles[currentOffset++] = (2 * random.nextInt(2) - 1) * 0.2f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0f;
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
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 6;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(0.65f, -0.8f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(0.65f, -0.3f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 2:
                    position = new Geometry.Point(-0.55f, 0.0f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 3:
                    position = new Geometry.Point(0.55f, 0.8f, 0f);
                    break;
                case 4:
                    position = new Geometry.Point(-0.6f, 0.85f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                default:
                    position = new Geometry.Point(-0.55f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            //方向随机,并且在glsl中取消重力加速度
            particles[currentOffset++] = (2 * random.nextInt(2) - 1) * 0.2f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }

    public void addParticlesThree() {
        Geometry.Point position;
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 6;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(0.6f, -0.9f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(-0.6f, -0.8f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 2:
                    position = new Geometry.Point(0.1f, -0.5f, 0f);

                    break;
                case 3:
                    position = new Geometry.Point(-0.5f, 0.1f, 0f);

                    break;
                case 4:
                    position = new Geometry.Point(0.6f, 0.1f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 5:
                    position = new Geometry.Point(0.55f, 0.8f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 3;//大小
                    break;
                default:
                    position = new Geometry.Point(-0.5f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            //方向随机,并且在glsl中取消重力加速度
            particles[currentOffset++] = (2 * random.nextInt(2) - 1) * 0.2f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }

    public void addParticlesFour() {
        Geometry.Point position;
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 6;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(-0.15f, -0.8f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(-0.65f, -0.4f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 2:
                    position = new Geometry.Point(0.55f, 0.1f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 3:
                    position = new Geometry.Point(-0.65f, 0.8f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 4:
                    position = new Geometry.Point(0.6f, 0.8f, 0f);
                    break;
                default:
                    position = new Geometry.Point(-0.55f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            //方向随机,并且在glsl中取消重力加速度
            particles[currentOffset++] = (2 * random.nextInt(2) - 1) * 0.2f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }

    public void addParticlesFive() {
        Geometry.Point position;
        for (int i = 0; i < num; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 6;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(-0.5f, -0.8f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小
                    break;
                case 1:
                    position = new Geometry.Point(0.6f, -0.4f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2;//大小

                    break;
                case 2:
                    position = new Geometry.Point(-0.6f, -0.25f, 0f);
                    break;
                case 3:
                    position = new Geometry.Point(0.5f, 0.1f, 0f);

                    break;
                case 4:
                    position = new Geometry.Point(-0.6f, 0.5f, 0f);
                    break;
                case 5:
                    position = new Geometry.Point(0.55f, 0.6f, 0f);
                    break;
                default:
                    position = new Geometry.Point(0.6f, 0.8f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            //方向随机,并且在glsl中取消重力加速度
            particles[currentOffset++] = (2 * random.nextInt(2) - 1) * 0.2f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0f;
            particles[currentOffset++] = 0;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;

    @IntDef({ONE, TWO, THREE, FOUR, FIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CLOUD {
    }

}
