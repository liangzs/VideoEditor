package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration5;

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
public class CDFiveSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;

    private static final int TOTAL_COUNT = POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_ALPHA_COUNT + TETURE_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    //暂定五朵烟花
    private int flowerNum = 3;
    private float alphaOffset;
    private static final float pointSize = 6;

    private int frameIndex;

    private int frameCount, indexOne, indexTwo;
    private List<float[]> listCoords;

    /**
     * indexOne
     *
     * @param duration
     */
    public CDFiveSystem(int duration) {
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);

        indexOne = (int) (frameCount * 0.2);
        alphaOffset = 1f / indexOne;
        indexTwo = (int) (frameCount * 0.76);
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
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

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
        int particleOffset;
        if (frameIndex < indexOne) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 4] += alphaOffset;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        } else if (frameIndex <= indexTwo) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 3] += pointSize;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
        if (indexTwo - 5 < frameIndex && frameIndex < frameCount) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 4] -= alphaOffset;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
        frameIndex++;
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
        frameIndex = 0;
        float[] temp;
        for (int i = 0; i < flowerNum; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            temp = createCoord(i);
            particles[currentOffset++] = temp[0];
            particles[currentOffset++] = temp[1];
            particles[currentOffset++] = 0;
            particles[currentOffset++] = (1 + random.nextInt(3)) * ConstantMediaSize.getMinWH() / 10;//大小
            particles[currentOffset++] = 0f;//alpha
            particles[currentOffset++] = i;//textureType
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }

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

    /**
     * 设置烟花数量
     */
    public void setNum(int num) {
        this.flowerNum = num;
    }

}
