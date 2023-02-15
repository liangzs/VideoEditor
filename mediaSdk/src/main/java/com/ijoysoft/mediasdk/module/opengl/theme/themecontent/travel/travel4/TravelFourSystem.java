package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel4;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleUtil;

import java.util.HashMap;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

public class TravelFourSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int ALPHA_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_SIZE_COUNT + POSITION_COMPONENT_COUNT
                    + TETURE_COMPONENT_COUNT + ALPHA_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private static final int haloSum = 8;//光圈数量
    private HashMap<Integer, Float> particleIndex;
    private HashMap<Integer, Float> particleOffset;//对应的移动速度
    private static final int frameCount = 50;//默认两秒50帧,40帧的时候开始逐渐淡化消失
    private int frameIndex;
    private int frameStart;//延迟帧数启动

    public TravelFourSystem() {
        particles = new float[haloSum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        particleIndex = new HashMap<>();
        particleOffset = new HashMap<>();
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPointSizeLocation(),
                1, STRIDE);
        dataOffset += PARTICLE_POINT_SIZE_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;


        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
        dataOffset += TETURE_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                ALPHA_COMPONENT_COUNT, STRIDE);

    }


    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        for (int i = 0; i < haloSum; i++) {
            final int offset = i * TOTAL_COUNT;
            int currentOffset = offset;
            if (Math.abs(particles[currentOffset + 2]) <= Math.abs(particleIndex.get(i))) {
                particles[currentOffset + 2] = particles[currentOffset + 2] + particleOffset.get(i);
                vertexArray.updateBuffer(particles, offset, TOTAL_COUNT);
            }
            //
            if (frameIndex > 65) {
                particles[currentOffset + 5] = particles[currentOffset + 5] - 0.1f < 0 ? 0 : particles[currentOffset + 5] - 0.1f;
            }
        }
    }

    /**
     * 1 top出现
     * -1 bottom出现
     *
     * @param invert
     */
    public void addParticles(float invert) {
        frameIndex = 0;
        Geometry.Point position;
        for (int i = 0; i < haloSum; i++) {
            final int offset = i * TOTAL_COUNT;
            int currentOffset = offset;
            //记录生命周期时间
            particles[currentOffset++] = ConstantMediaSize.getMinWH() / 5;//大小
            switch (i) {
                case 0:
                    position = new Geometry.Point(0f, -1f * invert, 0f);
                    particleIndex.put(i, 1f * invert);
                    particleOffset.put(i, 2f * invert / frameCount);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2.5f;
                    break;
                case 1:
                    position = new Geometry.Point(0f, -1.05f * invert, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 4;
                    particleIndex.put(i, 1.05f * invert);
                    particleOffset.put(i, 2.10f * invert / frameCount);
                    break;
                case 2:
                    position = new Geometry.Point(0f, -0.2f * invert, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 8;
                    particleIndex.put(i, 0.2f * invert);
                    particleOffset.put(i, 0.4f * invert / frameCount);
                    break;

                case 3:
                    position = new Geometry.Point(0f, 0.2f * invert, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 10;
                    particleIndex.put(i, -0.2f * invert);
                    particleOffset.put(i, -0.4f * invert / frameCount);
                    break;
                case 4:
                    position = new Geometry.Point(0, 0.5f * invert, 0f);
                    particleIndex.put(i, -0.7f * invert);
                    particleOffset.put(i, -1.2f * invert / frameCount);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 3;
                    break;
                case 5:
                    position = new Geometry.Point(0, 0.6f * invert, 0f);
                    particleIndex.put(i, -0.9f * invert);
                    particleOffset.put(i, -1.5f * invert / frameCount);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 7;
                    break;
                case 6:
                    position = new Geometry.Point(0, 0.7f * invert, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 2.2f;
                    particleIndex.put(i, -1.0f * invert);
                    particleOffset.put(i, -1.7f * invert / frameCount);
                    break;
                case 7:
                    position = new Geometry.Point(0, 0.9f * invert, 0f);
                    particles[currentOffset - 1] = ConstantMediaSize.getMinWH() / 1.5f;
                    particleIndex.put(i, -1.2f * invert);
                    particleOffset.put(i, -2.1f * invert / frameCount);
                    break;
                default:
                    position = new Geometry.Point(0f, 0f, 0f);
                    break;
            }
            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;
            particles[currentOffset++] = i;
            //alpha
            particles[currentOffset++] = 1f;
            vertexArray.updateBuffer(particles, offset, TOTAL_COUNT);
        }
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        frameIndex++;
        if (frameIndex > frameStart) {
            updateParticle();
            glDrawArrays(GL_POINTS, 0, haloSum);
        }
    }

    public void setDelayStart(long delayTime) {
        frameStart = (int) (delayTime * ConstantMediaSize.FPS / 1000);
    }


    /**
     * 降低蓝色花朵概率
     */
    private int evaluateFlowerType() {
        int a = random.nextInt(100);
        int b;
        if (a < 45) {
            b = 0;
        } else if (a < 90) {
            b = 1;
        } else {
            b = 2;
        }
        return b;
    }
}
