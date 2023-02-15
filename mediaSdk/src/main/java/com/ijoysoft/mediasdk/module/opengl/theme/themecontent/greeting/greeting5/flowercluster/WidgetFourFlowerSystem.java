package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.flowercluster;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import java.util.HashMap;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class WidgetFourFlowerSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_SIZE_COUNT + POSITION_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + TETURE_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private static final int flowerNum = 7;
    private static final float moveOffset = -0.5f;
    private float current;
    private float origin;
    private HashMap<Integer, Float> particleIndex;

    public WidgetFourFlowerSystem() {
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        particleIndex = new HashMap<>();
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

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        updateParticle();
        glDrawArrays(GL_POINTS, 0, flowerNum);
    }

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        for (int i = 0; i < flowerNum; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            if (particles[currentOffset] < particles[currentOffset + 1]) {
                current = particles[currentOffset] + 4;
                origin = particles[currentOffset + 1];
                current = current > origin ? origin : current;
                particles[currentOffset] = current;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }

            //特定控件移动
            if (i > 2) {
                if (particles[currentOffset + 3] < particleIndex.get(i)) {
                    particles[currentOffset + 3] = particles[currentOffset + 3] + 0.05f;
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
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
        Geometry.Point position;
        for (int i = 0; i < flowerNum; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            //记录生命周期时间
            particles[currentOffset++] = 0;//当前大小
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 5;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(-0.8f, 0.85f, 0f);
                    break;
                case 1:
                    position = new Geometry.Point(-0.6f, 0.75f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 3;
                    break;
                case 2:
                    position = new Geometry.Point(-0.5f, 0.9f, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 4;
                    break;

                case 3:
                    position = new Geometry.Point(0.65f, -0.9f+moveOffset, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 3;
                    particles[currentOffset - 2] = particles[currentOffset - 1];
                    particleIndex.put(3, -0.9f);
                    break;
                case 4:
                    position = new Geometry.Point(0.6f, -0.8f+moveOffset, 0f);
                    particles[currentOffset - 2] = particles[currentOffset - 1];
                    particleIndex.put(4, -0.8f);
                    break;
                case 5:
                    position = new Geometry.Point(0.9f, -0.7f+moveOffset, 0f);
                    particles[currentOffset - 2] = particles[currentOffset - 1];
                    particleIndex.put(5, -0.7f);
                    break;
                case 6:
                    position = new Geometry.Point(0.85f, -0.85f+moveOffset, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 3;
                    particles[currentOffset - 2] = particles[currentOffset - 1];
                    particleIndex.put(6, -0.85f);
                    break;
                default:
                    position = new Geometry.Point(-0.85f, 0.4f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;

            particles[currentOffset++] = 0;
            particles[currentOffset] = i;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }

    }

}
