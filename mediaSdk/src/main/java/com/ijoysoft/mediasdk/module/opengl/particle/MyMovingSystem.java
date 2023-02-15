package com.ijoysoft.mediasdk.module.opengl.particle;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

import java.util.Random;

/**
 * 粒子移动绘制
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneSystem}
 */
public class MyMovingSystem extends BaseParticleSystem {
    private static final int LIFETIME_COMPOMENT_COUNT = 2;//记录生命周期和消耗时间，在始末进行动画缩放大小
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    public static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TETURE_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + PARTICLE_ALPHA_COUNT;

    protected int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private int pointSize;



    /**
     * 素材种类数量
     */
    int textureCount;

    /**
     * 固定粒子最大时的大小
     */
    private boolean sizeFreeze;

    /**
     * 屏宽的多少倍
     */
    private float ratio = 0.01f;

    /**
     * 在第一帧就加载所有粒子
     * 一般用于单个Action中的drawer
     */
    private boolean loadAllParticleInFirstFrame;

    /**
     * 用自己的生成器加在第一帧
     */
    private boolean loadFirstFrameByGenerator;



    /**
     * 自定义颜色
     */
    private IParticle.ICreateColor colorCreator = null;

    /**
     * 粒子相对大小范围 [sizeFloatingStart, 1]
     */
    private float sizeFloatingStart = 4f/6f;

    /**
     * 随机透明度起始值
     * alpha = [randomAlphaStart, 1]
     */
    private float randomAlphaStart = 1f;


    /**
     * 构造
     * @param maxParticleCount 最大粒子数量
     * @param ratio 粒子大小相对值
     */
    public MyMovingSystem(int textureCount, int maxParticleCount, float ratio) {
        this.textureCount = textureCount;
        this.ratio = ratio;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }


    /**
     * 绑定数据
     * @param particleProgram
     */
    @Override
    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
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
            while (currentParticleCount < maxParticleCount) {
                addParticleImpl(currentParticleCount++, particleStartTime, true);
            }
        } else {
            if (currentParticleCount < maxParticleCount && random.nextInt(10) < 1) {
                addParticleImpl(currentParticleCount++, particleStartTime, true);
            }
        }
        for (int i = 0; i < currentParticleCount; i++) {
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
//        LogUtils.i(getClass().getSimpleName(), "particle updated");
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        positionUpdater.update(particles, currentOffset, 3);
        //出界重新更新位置
        if (particles[currentOffset] > 1f || particles[currentOffset] < -1f || particles[currentOffset + 1] > 1f || particles[currentOffset + 1] < -1f) {
            startPositionGenerator.setPosition(particles, currentOffset);
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
        //记录生命周期时间
        float temp = random.nextInt(4) + 1;
        //位置
        if (loadAllParticleInFirstFrame && currentParticleCount < maxParticleCount && !loadFirstFrameByGenerator) {
            //第一帧加载所有粒子时随机充满整个屏幕
            defaultRandomPosition.setPosition(particles, currentOffset);
        } else {
            startPositionGenerator.setPosition(particles, currentOffset);
        }
        currentOffset += 3;

        //大小
        temp = ConstantMediaSize.getMinWH() * ratio;//(20,25,30)
        if (sizeFreeze) {
            temp *= 6;
        } else {
            temp *= (6*sizeFloatingStart + random.nextFloat() * (6 * (1-sizeFloatingStart)));
        }
        particles[currentOffset++] = temp;
        particles[currentOffset++] = temp;

        //随机素材种类
        //增大随机数范围减少重复
        particles[currentOffset++] = random.nextInt(textureCount*10)/10;
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
        if (randomAlphaStart != 1.0f) {
            particles[currentOffset++] = randomAlphaStart + (1f - randomAlphaStart) * random.nextFloat();
        } else {
            particles[currentOffset++] = 1f;
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    public void setTextureCount(int textureCount) {
        this.textureCount = textureCount;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }



    public void setPositionUpdater(ParticlePositionUpdater positionUpdater) {
        this.positionUpdater = positionUpdater;
    }


    public void setSizeFreeze(boolean sizeFreeze) {
        this.sizeFreeze = sizeFreeze;
    }

    private ParticlePositionUpdater positionUpdater;

    /**
     * 粒子起点定义器
     */
    public interface ParticleStartPositionGenerator {
        /**
         * 设置粒子位置
         * @param particle 粒子数据数组
         * @param offset particle[offset]为x， particle[offset+1]为y, particle[offset+2]为z
         */
        void setPosition(float[] particle, int offset);
    }

    static ParticleStartPositionGenerator defaultRandomPosition = new ParticleStartPositionGenerator() {

        Random random = new Random();

        @Override
        public void setPosition(float[] particle, int offset) {
            particle[offset++] = random.nextFloat() * -2f + 1f;
            particle[offset++] = random.nextFloat() * -2f + 1f;
            particle[offset] = 0f;
        }
    };


    private ParticleStartPositionGenerator startPositionGenerator = defaultRandomPosition;


    public void setStartPositionGenerator(ParticleStartPositionGenerator startPositionGenerator) {
        this.startPositionGenerator = startPositionGenerator;
    }

    /**
     * 粒子位置更新器
     */
    public interface ParticlePositionUpdater {

        /**
         * 更新粒子位置
         * @param particle 粒子数据数组
         * @param offset particle[offset]为x， particle[offset+1]为y
         */
        void update(float[] particle, int offset, int length);
    }

    public void setLoadAllParticleInFirstFrame(boolean loadAllParticleInFirstFrame) {
        this.loadAllParticleInFirstFrame = loadAllParticleInFirstFrame;
    }

    /**
     * 用自己的生成器加在第一帧
     */
    public void setLoadFirstFrameByGenerator(boolean loadFirstFrameByGenerator) {
        this.loadFirstFrameByGenerator = loadFirstFrameByGenerator;
    }

    public void setColorCreator(IParticle.ICreateColor colorCreator) {
        this.colorCreator = colorCreator;
    }


    /**
     * 当前是否宽屏
     */
    protected boolean widescreen = false;




    public void onSizeChanged(int width, int height) {
        if (width >= height) {
            if (widescreen) {
                return;
            } else {
                widescreen = true;
                ratio *= 0.5f;
                for (int i = 3; i < particles.length; i += TOTAL_COUNT) {
                    particles[i] *= 0.5f;
                    particles[i+1] *= 0.5f;
                }
            }
        } else {
            if (!widescreen) {
                return;
            } else {
                widescreen = false;
                ratio *= 2f;
                for (int i = 3; i < particles.length; i += TOTAL_COUNT) {
                    particles[i] *= 2f;
                    particles[i+1] *= 2f;
                }
            }
        }
        vertexArray.updateBuffer(particles, 0, particles.length);
    }


    public void setSizeFloatingStart(float sizeFloatingStart) {
        this.sizeFloatingStart = sizeFloatingStart;
    }

    public void setRandomAlphaStart(float randomAlphaStart) {
        this.randomAlphaStart = randomAlphaStart;
    }
}
