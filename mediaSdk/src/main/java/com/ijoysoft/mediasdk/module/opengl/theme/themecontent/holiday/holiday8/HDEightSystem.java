package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday8;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 这里先做透明渐现，透明度1之后再做放大，放大结束后渐隐
 * 定义烟花2s，0.5渐现，1s 放大，0.5 渐隐
 */
public class HDEightSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int LIFE_COUNT = 2;

    private static final int TOTAL_COUNT = POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_ALPHA_COUNT + TETURE_COMPONENT_COUNT + LIFE_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    //暂定五朵烟花
    private int flowerNum = 5;
    private float alphaOffset = 0.1f;
    private static final float pointSize = 4;
    private List<float[]> listCoords;

    /**
     * indexOne
     */
    public HDEightSystem() {
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        listCoords = new ArrayList<>();
    }


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
     * 更新时间，更新大小
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentLife = particleOffset + TOTAL_COUNT - 2;
        current = particles[currentLife] = particles[currentLife] + 0.04f;
        if (current < 0.6) {
            particles[particleOffset + 4] += alphaOffset;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        } else if (current <= 2.0) {
            particles[particleOffset + 3] += pointSize;
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        } else if (current > 2.0 && current < 2.5f) {
            particles[particleOffset + 4] -= alphaOffset * 1.5f;
            particles[particleOffset + 4] = particles[particleOffset + 4] < 0f ? 0f : particles[particleOffset + 4];
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }
    }


    /**
     * 增加粒子
     */
    public void addParticle() {
        if (currentParticleCount < flowerNum) {
            addParticleImpl(currentParticleCount);
            currentParticleCount++;
            return;
        }
        for (int i = 0; i < flowerNum; i++) {
            int currentLife = i * TOTAL_COUNT + TOTAL_COUNT - 2;
            current = particles[currentLife] + 0.04f;

            if (current > particles[currentLife + 1]) {
                addParticleImpl(i);
                continue;
            }
            updateParticle(i);
        }
    }

    float[] temp;

    /**
     * 新增粒子
     */
    public void addParticleImpl(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        temp = createCoord(index);
        particles[currentOffset++] = temp[0];
        particles[currentOffset++] = temp[1];
        particles[currentOffset++] = 0;
        particles[currentOffset++] = (1 + random.nextInt(3)) * ConstantMediaSize.getMinWH() / 40;//大小
        particles[currentOffset++] = 0f;//alpha
        particles[currentOffset++] = random.nextInt(4);//textureType
        particles[currentOffset++] = 0;
        particles[currentOffset++] = random.nextInt(4) + 2.5f;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    private float[] createCoord(int index) {
        float[] xy = new float[2];
        xy[0] = randomVectex();
        xy[1] = randomVectex();
        while (!checkVectex(xy, index)) {
            xy[0] = randomVectex();
            xy[1] = randomVectex();
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
    private boolean checkVectex(float[] xy, int index) {
        for (int i = 0; i < index; i++) {
            if (Math.abs(listCoords.get(i)[0] - xy[0]) < 0.3 && Math.abs(listCoords.get(i)[1] - xy[1]) < 0.3) {
                return false;
            }
        }
        return true;
    }


}
