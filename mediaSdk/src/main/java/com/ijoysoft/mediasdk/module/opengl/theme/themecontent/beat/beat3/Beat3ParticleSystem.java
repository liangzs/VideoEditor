package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat3;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 三个粒子，如果为粒子1的话，则做一个颜色随机。三个粒子做一个概率分布
 */
public class Beat3ParticleSystem extends BaseParticleSystem {
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

    public Beat3ParticleSystem() {

    }

    public void setBuilder(ParticleBuilder builder) {
        this.builder = builder;
        maxParticleCount = builder.particleCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        scaleSpeed = ConstantMediaSize.getMinWH() / 20;
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
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime);
                continue;
            }
            updateParticle(i);
        }
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
        float pointSize = ConstantMediaSize.getMinWH() / 5 + (random.nextInt(2) == 0 ? 50 : 0);
        if (builder.isScale) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = pointSize;//当前大小
        }
        //pointsize
        particles[currentOffset++] = pointSize;
        //locaion
        Geometry.Point position = createPoint(index);
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = 0;
        particles[currentOffset++] = 0;
        particles[currentOffset++] = 0f;

        float[] color = randomColor();
        /*颜色*/
        particles[currentOffset++] = color[0];
        particles[currentOffset++] = color[1];
        particles[currentOffset++] = color[2];
        //time
        particles[currentOffset++] = particleStartTime;
        //纹理
        particles[currentOffset++] = 0;
        particles[currentOffset++] = 1f;//alpha
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    @Override

    public void onDrawFrame(ParticleShaderProgram particleProgram, float currentTime) {
        addParticle(currentTime);
        bindData(particleProgram);
        draw();
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
            //254,201,73
            result[0] = 254f / 255f;
            result[1] = 201f / 255f;
            result[2] = 73f / 255f;
        } else if (a < 50) {
            //255,150,239
            result[0] = 1;
            result[1] = 150f / 255f;
            result[2] = 239f / 255f;
        } else if (a < 75) {
            //254,73,226
            result[0] = 254f / 255f;
            result[1] = 73f / 255f;
            result[2] = 226f / 255f;
        } else {
            //255,224,150
            result[0] = 1f;
            result[1] = 224f / 255f;
            result[2] = 150f / 255f;
        }
        return result;
    }


    /**
     * 创建固定点
     * 一共六个点
     */
    private Geometry.Point createPoint(int index) {
        switch (index) {
            case 0:
                return new Geometry.Point(-0.8f, -0.5f, 0);
            case 1:
                return new Geometry.Point(-0.6f, -0.6f, 0);
            case 2:
                return new Geometry.Point(-0.2f, -0.55f, 0);
            case 3:
                return new Geometry.Point(0.1f, -0.65f, 0);
            case 4:
                return new Geometry.Point(0.6f, -0.7f, 0);
            case 5:
                return new Geometry.Point(0.8f, -0.6f, 0);
        }
        return new Geometry.Point(-0.8f, -0.5f, 0);
    }
}
