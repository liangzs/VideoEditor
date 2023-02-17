package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle;

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
public class BDThreeWidgetOneSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int TOTAL_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + PARTICLE_ALPHA_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    //暂定五朵烟花
    private static final int flowerNum = 5;
    private static final float alphaOffset = 0.0834f;
    private static final float pointSize = 5;

    private int frameIndex;

    private int frameCount, indexOne, indexTwo;
    private List<float[]> listCoords;

    /**
     * indexOne
     *
     * @param duration
     */
    public BDThreeWidgetOneSystem(int duration) {
        particles = new float[flowerNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        frameCount =(int) Math.ceil( duration * ConstantMediaSize.FPS / 1000f);
        indexOne = (int) (frameCount * 0.33);
        indexTwo = (int) (frameCount * 0.66);
        listCoords = new ArrayList<>();
    }


    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                PARTICLE_POINT_SIZE_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_ALPHA_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        glDrawArrays(GL_POINTS, 0, flowerNum);
        updateParticle();
    }

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        int particleOffset;
        if (frameIndex < indexOne) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 7] += alphaOffset;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        } else if (frameIndex <= indexTwo) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 6] += pointSize;
                vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            }
        }
        if (indexTwo - 5 < frameIndex && frameIndex < frameCount) {
            for (int i = 0; i < flowerNum; i++) {
                particleOffset = i * TOTAL_COUNT;
                particles[particleOffset + 7] -= alphaOffset;
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
            float[] colors = randomColor();
            particles[currentOffset++] = colors[0];
            particles[currentOffset++] = colors[1];
            particles[currentOffset++] = colors[2];
            particles[currentOffset++] = (1 + random.nextInt(3)) * ConstantMediaSize.getMinWH() / 15;//大小
            particles[currentOffset++] = 0f;//alpha
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
        }

    }

    /**
     * 等概率生成颜色
     * 7eff81
     * ff5980
     * ffd217
     *
     * @return
     */
    private float[] randomColor() {
        int a = random.nextInt(100);
        float[] result = new float[3];
        if (a < 33.33) {
            result[0] = 126f / 255f;
            result[1] = 1f;
            result[2] = 129 / 255f;
        } else if (a < 66.66) {
            result[0] = 1f;
            result[1] = 89 / 255f;
            result[2] = 128 / 255f;
        } else {
            result[0] = 1f;
            result[1] = 210 / 255f;
            result[2] = 23 / 255f;
        }
        return result;
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