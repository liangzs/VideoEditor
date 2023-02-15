package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局烟花
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixSystem}
 */
public class ScaleFadeSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int LIFE_COUNT = 2;

    private static final int TOTAL_COUNT = POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_ALPHA_COUNT + TETURE_COMPONENT_COUNT + LIFE_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    //暂定烟花数量
    private int flowerNum = 8;

    private float alphaOffset = 0.1f;

    /**
     * 大小差
     */
    private float pointSize = 2;
    private List<float[]> listCoords;


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
     * 固定粒子最大时的大小
     */
    private boolean sizeFree;

    /**
     * 粒子相对大小范围 [sizeFloatingStart, 1]
     */
    private float sizeFloatingStart = 0.3333333333f;

    /**
     * @param textureCount 素材种类数量
     */
    public ScaleFadeSystem(int textureCount) {
        this.textureCount = textureCount;
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }

    /**
     * @param textureCount 素材种类数量
     * @param flowerCount 最大烟花数量
     */
    public ScaleFadeSystem(int textureCount, int flowerCount) {
        this.textureCount = textureCount;
        this.flowerNum = flowerCount;
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }

    /**
     * @param textureCount 素材种类数量
     * @param flowerCount 最大烟花数量
     * @param delta 变大速度
     */
    public ScaleFadeSystem(int textureCount, int flowerCount, float delta) {
        pointSize = delta/2f;
        this.textureCount = textureCount;
        this.flowerNum = flowerCount;
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }

//    /**
//     * @param textureCount 素材种类数量
//     * @param flowerCount 最大烟花数量
//     * @param delta 变大速度
//     * @param maxSize 最大大小
//     */
//    public ScaleFadeSystem(int textureCount, int flowerCount, float delta, int maxSize) {
//        pointSize = delta;
//        this.textureCount = textureCount;
//        this.flowerNum = flowerCount;
//        particles = new float[flowerNum * TOTAL_COUNT];
//        vertexArray = new ParticleUtil(particles);
//        listCoords = new ArrayList<>();
//    }





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
        glDrawArrays(GL_POINTS, 0, flowerNum);
    }

    private float current;

    /**
     * 短生命快闪粒子
     */
    private Float shortLife = null;

    /**
     * 更新时间，更新大小
     */
    void updateParticle() {

        addParticle();
        for (int i = 0; i < currentParticleCount; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentLife = particleOffset + TOTAL_COUNT - 2;
            current = particles[currentLife] + 0.04f;
            if (current > particles[currentLife + 1]) {
                //LogUtils.v(getClass().getSimpleName(), "particle index:" + i + " new life");
                addParticleImpl(i);
                continue;
            }
//        LogUtils.i(getClass().getSimpleName(), "particle updated");
            current = particles[currentLife] = particles[currentLife] + 0.04f;
            if (shortLife == null) {
                if (current < 0.6) {
                    particles[particleOffset + 4] += alphaOffset;
                    particles[particleOffset + 4] = particles[particleOffset + 4] > 1f ? 1f : particles[particleOffset + 4];
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                } else if (current <= particles[currentLife + 1] - 0.5) {
                    particles[particleOffset + 3] += pointSize;
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                } else if (current + 0.5f > particles[currentLife + 1]) {
                    particles[particleOffset + 4] -= alphaOffset * 1.5f;
                    particles[particleOffset + 4] = particles[particleOffset + 4] < 0f ? 0f : particles[particleOffset + 4];
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
            } else {
                if (current < shortLife * 0.666f) {
                    particles[particleOffset + 3] += pointSize;
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                } else {
                    particles[particleOffset + 4] -= alphaOffset * 1.5f;
                    particles[particleOffset + 4] = particles[particleOffset + 4] < 0f ? 0f : particles[particleOffset + 4];
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
            }
//            LogUtils.i(getClass().getSimpleName(), "alpha = " + particles[particleOffset + 4]);
        }
    }


    /**
     * 增加粒子
     * 一次增加所有粒子
     */
    public void addParticle() {
        if (loadAllParticleInFirstFrame) {
            while (currentParticleCount < flowerNum ) {
                addParticleImpl(currentParticleCount++);
            }
        } else {
            if (shortLife == null) {

                //随机增加粒子，不要一开始就渲染全部粒子
                if (currentParticleCount < flowerNum && random.nextInt(10) < 1) {
                    addParticleImpl(currentParticleCount++);
                }
            } else {
                //随机增加粒子，不要一开始就渲染全部粒子
                if (currentParticleCount < flowerNum && random.nextInt(6) < 1) {
                    addParticleImpl(currentParticleCount++);
                }
            }
        }

    }

    float[] temp;

    /**
     * 新增粒子
     */
    public void addParticleImpl(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        //位置，不要贴太近
        temp = createCoord(index);
        particles[currentOffset++] = temp[0];
        particles[currentOffset++] = temp[1];
        //大小
        particles[currentOffset++] = 0;
        if (sizeFree) {
            particles[currentOffset++] = 3 * ConstantMediaSize.getMinWH() / 40;
        } else {
            particles[currentOffset++] = (3 * (sizeFloatingStart + (1-sizeFloatingStart) * random.nextFloat())) * ConstantMediaSize.getMinWH() / 40;
        }
        //alpha
        particles[currentOffset++] = shortLife == null ? 0f : 1f;
        //随机素材种类
        //增大随机数范围减少重复
        particles[currentOffset++] = random.nextInt(textureCount * 10) / 10;
        //生命周期随机
        particles[currentOffset++] = 0;
        if (shortLife == null) {
            float life = random.nextInt(3) + 1.5f;
            LogUtils.v(getClass().getSimpleName(), "random life: " + life);
            particles[currentOffset++] = life;
        } else {
            particles[currentOffset++] = shortLife;
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    private float[] createCoord(int index) {
        float[] xy = new float[2];
        xy[0] = randomVectex();
        xy[1] = randomVectex();
        //粒子数量多时可能造成实际上而非逻辑上的死循环，改用传入距离，每次减少一半
        float delta = 0.3f;
        while (!checkVectex(xy, index, delta)) {
            for (int i = 0; i < 8; i++) {
                xy[0] = randomVectex();
                xy[1] = randomVectex();
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

    private float randomVectex() {
        int a = random.nextInt(100);
        float result;
        if (a < 50) {
            result = 1f - 0.7f * random.nextFloat();
        } else {
            result = 0.7f * random.nextFloat() - 1f;
        }
        return result;
    }

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

    public void setSizeFree(boolean sizeFree) {
        this.sizeFree = sizeFree;
    }


    public void setLoadAllParticleInFirstFrame(boolean loadAllParticleInFirstFrame) {
        this.loadAllParticleInFirstFrame = loadAllParticleInFirstFrame;
    }

    public void setShortLife(Float shortLife) {
        this.shortLife = shortLife;
        this.loadAllParticleInFirstFrame = false;
    }
}
