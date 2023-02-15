package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 渐变出现和消失
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixSystem}
 */
public class RandomFadeSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int LIFE_COUNT = 2;

    private static final int TOTAL_COUNT = POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_ALPHA_COUNT + TETURE_COMPONENT_COUNT + LIFE_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;



    /**
     * 素材种类数量
     */
    int textureCount;

    /**
     * 固定粒子最大时的大小
     */
    private boolean sizeFreeze;

    /**
     * 大小比例
     */
    private float ratio = 0.025f;

    /**
     * 透明度变化单位
     */
    private float alphaOffset = 0.15f;

    /**
     * 在第一帧就加载所有粒子
     * 一般用于单个Action中的drawer
     */
    private boolean loadAllParticleInFirstFrame;

    /**
     * 关闭渐变
     */
    private boolean disableFade;

    private static final float pointSize = 4;

    private List<float[]> listCoords;

    float[] randomMaxAlpha;

    /**
     * @param textureCount 素材种类数量
     */
    public RandomFadeSystem(int textureCount) {
        this.textureCount = textureCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
        maxParticleCount = 16;
    }

    /**
     * 构造器
     * @param textureCount 素材种类数量
     * @param flowerCount 粒子数量
     */
    public RandomFadeSystem(int textureCount, int flowerCount) {
        this.textureCount = textureCount;
        this.maxParticleCount = flowerCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }

    /**
     * 粒子相对大小范围 [sizeFloatingStart, 1]
     */
    private float sizeFloatingStart = 0.5f;

    /**
     * 构造器
     * @param textureCount 素材种类数量
     * @param flowerCount 粒子数量
     * @param ratio 粒子大小比例
     */
    public RandomFadeSystem(int textureCount, int flowerCount, float ratio) {
        this.ratio = ratio;
        this.textureCount = textureCount;
        this.maxParticleCount = flowerCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }


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
                particleProgram.getAlphaLocation(),
                PARTICLE_ALPHA_COUNT, STRIDE);
        dataOffset += PARTICLE_ALPHA_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, maxParticleCount);
    }

    private float current;

    /**
     * 更新时间，更新大小
     */
    void updateParticle() {

        if (loadAllParticleInFirstFrame) {
            while (currentParticleCount < maxParticleCount ) {
                addParticleImpl(currentParticleCount++);
            }
        } else {
            //随机增加粒子，不要一开始就渲染全部粒子
            if (currentParticleCount < maxParticleCount && random.nextInt(10) < 1) {
                addParticleImpl(currentParticleCount++);
            }
        }


        for (int i = 0; i < currentParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentLife = particleOffset + TOTAL_COUNT - 2;
            current = particles[currentLife] + 0.04f;
            if (current > particles[currentLife + 1]) {
                //LogUtils.v(getClass().getSimpleName(), "particle index:" + i + " new life");
                addParticleImpl(i);
                continue;
            }


            current = particles[currentLife] = particles[currentLife] + 0.04f;
            if (i == 0) {
                LogUtils.v(getClass().getSimpleName()+"#particles[0]", Arrays.toString(Arrays.copyOf(particles,8)));
            }
            if (disableFade) {
                continue;
            }
            if (current < 0.6) {
                if (particles[particleOffset + 4] < 1f) {
                    particles[particleOffset + 4] += alphaOffset;
                    if (randomMaxAlpha == null) {
                        particles[particleOffset + 4] = Math.min(particles[particleOffset + 4], 1f);
                    } else {
                        particles[particleOffset + 4] = Math.min(particles[particleOffset + 4], randomMaxAlpha[i]);
                    }

                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
            } else if (current > 2.0 && current < 2.5f) {
                if (particles[particleOffset + 4] > 0f) {
                    particles[particleOffset + 4] -= alphaOffset;
                    particles[particleOffset + 4] = Math.max(particles[particleOffset + 4], 0f);
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
            } else {
                if (particles[particleOffset + 4] != 1f) {
                    particles[particleOffset + 4] = 1f;
                }
            }
        }
    }




    /**
     * 新增粒子
     */
    public void addParticleImpl(int index) {
        LogUtils.v(getClass().getSimpleName() + "#addParticleImpl", "index:" + index);
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        float[] xy = createCoord(index);
        //随机粒子，随机位置
        particles[currentOffset++] = xy[0];
        particles[currentOffset++] = xy[1];
        particles[currentOffset++] = 0;

        //随机大小
        particles[currentOffset] = 3 * ConstantMediaSize.getMinWH() * ratio;
        if (!sizeFreeze) {
            particles[currentOffset] *= sizeFloatingStart + (1-sizeFloatingStart) * random.nextFloat();
        }
        currentOffset++;
        //初始透明度alpha
        particles[currentOffset++] = disableFade ? 1f : 0f;
        //随机粒子种类
        //增大随机数范围减少重复
        particles[currentOffset++] = random.nextInt(textureCount*10)/10;
        //生命周期
        particles[currentOffset++] = 0;
        particles[currentOffset++] = 1.5f + random.nextFloat();
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);

        //0.3-1.0随机最大透明度,有三成的可能为不透明
        if (randomMaxAlpha != null) {
            randomMaxAlpha[index] = random.nextFloat() + 0.3f;
            randomMaxAlpha[index] = Math.min(randomMaxAlpha[index], 1f);
        }
    }


    private float[] createCoord(int index) {
        float[] xy = new float[2];
        xy[0] = 2f * (random.nextFloat() - 0.5f);//randomVectex();
        xy[1] = 2f * (random.nextFloat() - 0.5f);//randomVectex();
        //粒子数量多时可能造成实际上而非逻辑上的死循环，改用传入距离，每次减少一半
        float delta = 0.3f;
        while (!checkVectex(xy, index, delta)) {
            for (int i = 0; i < 8; i++) {
                xy[0] = 2f * (random.nextFloat() - 0.5f);//randomVectex();
                xy[1] = 2f * (random.nextFloat() - 0.5f);//randomVectex();
                if (checkVectex(xy, index, delta)) {
                    listCoords.add(index, xy);
                    return xy;
                }
            }
            delta /= 2f;
        }
        listCoords.add(index, xy);
        return xy;
    }

//    private float randomVectex() {
//        int a = random.nextInt(100);
//        float result;
//        if (a < 50) {
//            result = 1f - 0.7f * random.nextFloat();
//        } else {
//            result = 0.7f * random.nextFloat() - 1f;
//        }
//        return result;
//    }

    /**
     * 检查生成点与现有的是否挨的太近了
     *
     * @param index
     * @return
     */
    private boolean checkVectex(float[] xy, int index, float delta) {
        for (int i = 0; i < index; i++) {
            if (Math.abs(listCoords.get(i)[0] - xy[0]) < delta && Math.abs(listCoords.get(i)[1] - xy[1]) < delta) {
                return false;
            }
        }
        return true;
    }

    public void setSizeFreeze(boolean sizeFreeze) {
        this.sizeFreeze = sizeFreeze;
    }

    /**
     * 开启随机最大透明度
     * @param enable 开启
     */
    public void setRandomAlpha(boolean enable) {
        if (enable) {
            randomMaxAlpha = new float[maxParticleCount];
        }
    }

    public void setLoadAllParticleInFirstFrame(boolean loadAllParticleInFirstFrame) {
        this.loadAllParticleInFirstFrame = loadAllParticleInFirstFrame;
    }

    /**
     * 关闭渐变
     */
    public void setDisableFade(boolean disableFade) {
        this.disableFade = disableFade;
    }
}
