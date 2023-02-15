package com.ijoysoft.mediasdk.module.opengl.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 模拟蝴蝶飞行，即做一个纹理切换，实现动态的gif操作
 */
public class FlyParticleSystem extends BaseParticleSystem {
    //记录生命周期和消耗时间，在始末进行动画缩放大小，只做数据记录，不进行opengl数据的传导
    private static final int LIFETIME_COMPOMENT_COUNT = 2;
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    //把能加的都加上去，用的值就取更新。不用到忽略
    private static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    PARTICLE_POINT_SIZE_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + TETURE_COMPONENT_COUNT + PARTICLE_ALPHA_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private ParticleBuilder builder;
    private float currentLife;
    private float current;
    private float origin;
    private float temp;
    private float scaleSpeed;//放大的速度

    public FlyParticleSystem() {

    }

    public void setBuilder(ParticleBuilder builder) {
        this.builder = builder;
        maxParticleCount = builder.particleCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        scaleSpeed = ConstantMediaSize.getMinWH() / (4f * builder.basePointSize);
    }

    public void bindData(ParticleShaderProgram particleProgram) {
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
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_ALPHA_COUNT, STRIDE);
    }


    public void addParticle(float particleStartTime) {
//        pointSize = ConstantMediaSize.getMinWH() / 60;
        if (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, particleStartTime);
            currentParticleCount++;
            return;
        }
        for (int i = 0; i < maxParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.04f;
            particles[currentOffset] = currentLife;
            updateTexture(particleOffset);
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime);
                continue;
            }
            updateParticle(i);
        }
    }

    private void updateTexture(int particleOffset) {
        int particleIndex = particleOffset + TOTAL_COUNT - 2;
        float currentParticle = particles[particleIndex];
        particles[particleIndex] = (currentParticle + 1f) % 3f;
    }

    /**
     * 更新时间，更新大小
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        if (builder.isScale) {//缩放动作
            updateParticleScale(particleOffset);
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 缩放操作
     *
     * @param currentOffset
     */
    private void updateParticleScale(int currentOffset) {
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset];
        if (current < 0.3) {//准备消失
            temp = particles[currentOffset + 2] - scaleSpeed;
            temp = temp < 0 ? 0 : temp;
            particles[currentOffset + 2] = temp;
        }
        if (origin - current < 0.3) {
            current = particles[currentOffset + 2] + scaleSpeed;
            origin = particles[currentOffset + 3];
            current = current > origin ? origin : current;
            particles[currentOffset + 2] = current;
        }
    }

    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        if (builder != null && builder.getiAddParticle() != null) {
            builder.getiAddParticle().addParticle(particles, particleOffset, particleStartTime, builder);
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            return;
        }
        addParticleHover(index, particleStartTime);
    }

    /**
     * 悬停散播
     */
    private void addParticleHover(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        //记录生命周期时间
        float temp = random.nextInt(6) + 1;
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期
        float pointSize = (random.nextInt(builder.randomPointSize) + 1) * (ConstantMediaSize.getMinWH() / builder.basePointSize);
        if (builder.isScale) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = pointSize;//当前大小
        }
        //pointsize
        particles[currentOffset++] = pointSize;
        //locaion
        Geometry.Point position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.2f;
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.5f;
        particles[currentOffset++] = 0f;

        int particle = randomParticle();
        float[] color = new float[]{0, 0, 0};
        if (particle == 0) {
            color = randomColor();
        }
        /*颜色*/
        particles[currentOffset++] = color[0];
        particles[currentOffset++] = color[1];
        particles[currentOffset++] = color[2];
        //time
        particles[currentOffset++] = particleStartTime;
        //纹理
        particles[currentOffset++] = particle;
        particles[currentOffset++] = 1f;//alpha
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    @Override

    public void onDrawFrame(ParticleShaderProgram particleProgram, float currentTime) {
        addParticle(currentTime);
        bindData(particleProgram);
        draw();
    }


    private int randomParticle() {
        int a = random.nextInt(100);
        if (a < 60) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 等概率生成颜色
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 33) {
            result[0] = 241f / 255f;
            result[1] = 24f / 255f;
            result[2] = 1f;
        } else if (a < 66) {
            result[0] = 0;
            result[1] = 150f / 255f;
            result[2] = 1;
        } else {
            result[0] = 1f;
            result[1] = 201f / 255f;
            result[2] = 15f / 255f;
        }
        return result;
    }
}
