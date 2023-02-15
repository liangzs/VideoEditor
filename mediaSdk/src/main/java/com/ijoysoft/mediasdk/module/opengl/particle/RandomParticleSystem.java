package com.ijoysoft.mediasdk.module.opengl.particle;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

/**
 * 随机粒子绘制
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneSystem}
 */
public class RandomParticleSystem extends BaseParticleSystem {
    private static final int LIFETIME_COMPOMENT_COUNT = 2;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + PARTICLE_ALPHA_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private int pointSize;


    /**
     * 固定粒子最大时的大小
     */
    private boolean sizeFree;

    /**
     * 粒子相对大小范围 [sizeFloatingStart, 1]
     */
    private float sizeFloatingStart = 4f/6f;

    /**
     * 在第一帧就加载所有粒子
     * 一般用于单个Action中的drawer
     */
    private boolean loadAllParticleInFirstFrame;


    /**
     * 素材种类数量
     */
    int textureCount;


    /**
     * 屏宽的多少倍
     */
    private float ratio = 0.01f;

    /**
     * 构造
     * @param maxParticleCount 最大粒子数量
     */
    public RandomParticleSystem(int textureCount, int maxParticleCount) {
        this.textureCount = textureCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    /**
     * 构造
     * @param maxParticleCount 最大粒子数量
     * @param ratio 粒子大小相对值
     */
    public RandomParticleSystem(int textureCount, int maxParticleCount, float ratio) {
        this.textureCount = textureCount;
        this.ratio = ratio;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }


    /**
     * 绑定数据
     * @param particleProgram
     * @param particleLocation
     */
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
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleLocation,
                TETURE_COMPONENT_COUNT, STRIDE);
        dataOffset += TETURE_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_ALPHA_COUNT, STRIDE);
//        dataOffset += PARTICLE_ALPHA_COUNT;
    }


    private float currentLife;

    public void addParticle(float particleStartTime) {
        pointSize = ConstantMediaSize.getMinWH() / 60;
        if (loadAllParticleInFirstFrame) {
            while (currentParticleCount < maxParticleCount ) {
                addParticleImpl(currentParticleCount, particleStartTime, true);
                currentParticleCount++;
            }
        } else {
            if (currentParticleCount < maxParticleCount && random.nextInt(10) < 1) {
                addParticleImpl(currentParticleCount, particleStartTime, true);
                currentParticleCount++;
            }
        }
        for (int i = 0; i < currentParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, particleStartTime, false);
                continue;
            }
            updateParticle(i, particleStartTime);
        }
    }

    private float current;
    private float origin;
    private float temp;


    /**
     * 撞击屏幕边缘反弹
     * @value true 粒子撞击屏幕边缘反弹
     * @value false 粒子离开屏幕后消失
     */
    private boolean rebound;

    /**
     * 更新时间，更新大小
     */
    private void updateParticle(int index, float particleStartTime) {
//        LogUtils.i(getClass().getSimpleName(), "particle updated");
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset] = current - 0.03f;
        if (current < 0.3) {//准备消失
            temp = particles[currentOffset + 2] - pointSize;
            temp = temp < 0 ? 0 : temp;
            particles[currentOffset + 2] = temp;
        }
        if (origin - current < 0.3) {
            current = particles[currentOffset + 2] + pointSize;
            origin = particles[currentOffset + 3];
            current = current > origin ? origin : current;
            particles[currentOffset + 2] = current;
        }
        //更新粒子位置
        particles[currentOffset + 4] += particles[currentOffset + 7];
        particles[currentOffset + 5] += particles[currentOffset + 8];

        if (particles[currentOffset + 4] < -1 || 1 < particles[currentOffset + 4]) {
            if (rebound) {
                //撞击屏幕边缘后反弹
                particles[currentOffset + 7] *= -1;
                particles[currentOffset + 4] += 2 * particles[currentOffset + 7];
            } else {
                //直接消失，更换新粒子
                addParticleImpl(index, particleStartTime, false);
                return;
            }
        }

        if (particles[currentOffset + 5] < -1 || 1 < particles[currentOffset + 5]) {
            if (rebound) {
                //撞击屏幕边缘后反弹
                particles[currentOffset + 8] *= -1;
                particles[currentOffset + 5] += 2 * particles[currentOffset + 8];
            } else {
                //直接消失，更换新粒子
                addParticleImpl(index, particleStartTime, false);
                return;
            }
        }

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 自定义颜色
     */
    private IParticle.ICreateColor colorCreator = null;

    /**
     * 随机透明度起始值
     * alpha = [randomAlphaStart, 1]
     */
    private float randomAlphaStart = 1f;

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

        temp = ConstantMediaSize.getMinWH() * ratio;//(20,25,30)
        if (!sizeFree) {
            temp *= (6*sizeFloatingStart + random.nextFloat() * (6 * (1-sizeFloatingStart)));
        } else {
            temp *= 6;
        }
        //当前大小
        particles[currentOffset++] = 0;
        particles[currentOffset++] = temp;//大小

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.02f;
        particles[currentOffset++] = (random.nextFloat() - 0.5f) * 0.02f;
        particles[currentOffset++] = 0f;


        particles[currentOffset++] = particleStartTime;
        //随机素材种类
        //增大随机数范围减少重复
        particles[currentOffset++] = random.nextInt(textureCount*10)/10;
        //原始颜色或随机颜色
        if (colorCreator == null) {
            particles[currentOffset++] = 1f;
            particles[currentOffset++] = 1f;
            particles[currentOffset++] = 1f;
        } else {
            float[] color = colorCreator.createColor();
            particles[currentOffset++] = color[0];
            particles[currentOffset++] = color[1];
            particles[currentOffset++] = color[2];
        }
        //透明度
        particles[currentOffset++] = randomAlphaStart + (1-randomAlphaStart) * random.nextFloat();

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    public void setSizeFree(boolean sizeFree) {
        this.sizeFree = sizeFree;
    }

    public void setSizeFloatingStart(float sizeFloatingStart) {
        this.sizeFloatingStart = sizeFloatingStart;
    }

    public void setLoadAllParticleInFirstFrame(boolean loadAllParticleInFirstFrame) {
        this.loadAllParticleInFirstFrame = loadAllParticleInFirstFrame;
    }

    /**
     * 撞击屏幕边缘反弹
     * @value true 粒子撞击屏幕边缘反弹
     * @value false 粒子离开屏幕后消失
     */
    public void setRebound(boolean rebound) {
        this.rebound = rebound;
    }

    public void setColorCreator(IParticle.ICreateColor colorCreator) {
        this.colorCreator = colorCreator;
    }

    public void setRandomAlphaStart(float randomAlphaStart) {
        this.randomAlphaStart = randomAlphaStart;
    }
}
