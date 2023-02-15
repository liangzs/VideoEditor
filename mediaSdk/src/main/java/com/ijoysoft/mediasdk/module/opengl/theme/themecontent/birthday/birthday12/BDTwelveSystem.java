package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class BDTwelveSystem extends BaseParticleSystem {
    private static final int LIFETIME_COMPOMENT_COUNT = 2;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    public BDTwelveSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void bindData(ParticleShaderProgram particleProgram, int particleLocation) {
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
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleLocation,
                TETURE_COMPONENT_COUNT, STRIDE);
    }


    private float currentLife;

    public void addParticle(float particleStartTime) {
        if (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, particleStartTime, true);
            currentParticleCount++;
            return;
        }
        for (int i = 0; i < maxParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime, false);
                continue;
            }
            updateParticle(i);
        }
    }

    private float current;
    private float origin;
    private float temp;

    /**
     * 更新时间，更新大小
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset] = current - 0.03f;
        if (current < 0.3) {//准备消失
            temp = particles[currentOffset + 2] - 4;
            temp = temp < 0 ? 0 : temp;
            particles[currentOffset + 2] = temp;
        }
        if (origin - current < 0.3) {
            current = particles[currentOffset + 2] + 4;
            origin = particles[currentOffset + 3];
            current = current > origin ? origin : current;
            particles[currentOffset + 2] = current;
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime, boolean isInit) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        Geometry.Point position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
        //记录生命周期时间
        float temp = random.nextInt(4) + 1;
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期

        temp = (4 + random.nextInt(3)) * ConstantMediaSize.getMinWH() / 15;//(20,25,30)
        if (isInit) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = 0;//当前大小
        }
        particles[currentOffset++] = temp;//大小

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.2f;
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.2f;
        particles[currentOffset++] = 0f;

        //颜色随机
        float[] colors = randomColor();
        particles[currentOffset++] = colors[0];
        particles[currentOffset++] = colors[1];
        particles[currentOffset++] = colors[2];

        particles[currentOffset++] = particleStartTime;
        particles[currentOffset++] = random.nextInt(3) * 1f;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    /**
     * 等概率生成颜色
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 25) {
            result[0] = 235f / 255f;
            result[1] = 0;
            result[2] = 139f / 255f;
        } else if (a < 50) {
            result[0] = 27f / 255f;
            result[1] = 117f / 255f;
            result[2] = 187f / 255f;
        } else if (a < 75) {
            result[0] = 35f / 255f;
            result[1] = 187f / 255f;
            result[2] = 184f / 255f;
        } else {
            result[0] = 1f;
            result[1] = 210f / 255f;
            result[2] = 0;
        }
        return result;
    }
}
