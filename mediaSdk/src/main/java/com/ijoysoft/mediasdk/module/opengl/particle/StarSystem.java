package com.ijoysoft.mediasdk.module.opengl.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 心形，v形，一形
 * fork from {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarSystem}
 */
public class StarSystem extends BaseParticleSystem {
    private static final int PARTICLE_POINT_LIFETIME_COUNT = 1;
    private static final int PARTICLE_POINT_ALPHA_COUNT = 1;
    private static final int PARTICLE_POINT_SIZE_COUNT = 1;
    private static final int TEXTURE_COMPONENT_COUNT = 1;
    private static final int TOTAL_COUNT =
            PARTICLE_POINT_LIFETIME_COUNT + PARTICLE_POINT_ALPHA_COUNT + POSITION_COMPONENT_COUNT + PARTICLE_POINT_SIZE_COUNT + TEXTURE_COMPONENT_COUNT;


    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;

    private int starNum = 20;
    //    private float x;
//    private float y;
    int starType;

    public StarSystem(int starType) {
        switch (starType) {
            case HEART:
                starNum = 100;
                break;
            case V_SHADE:
                starNum = 40;
                break;
        }
        particles = new float[starNum * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.starType = starType;
    }

    /**
     * 相对坐标（中心坐标） X
     */
    private float centerX = 0f;

    /**
     * 相对坐标（中心坐标） Y
     */
    private float centerY = 0f;

    /**
     * 大小缩放
     */
    private float ratio = 1f;


    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = PARTICLE_POINT_LIFETIME_COUNT;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_POINT_ALPHA_COUNT, STRIDE);
        dataOffset += PARTICLE_POINT_ALPHA_COUNT;

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
                TEXTURE_COMPONENT_COUNT, STRIDE);
    }

    /**
     * 复写父类方法
     */
    @Override
    public void draw() {
        updateParticle();
        glDrawArrays(GL_POINTS, 0, starNum);
    }

    private float currentLife;

    /**
     * 更新时间，更新大小
     */
    private void updateParticle() {
        for (int i = 0; i < starNum; i++) {
            final int particleOffset = i * TOTAL_COUNT;
            int currentOffset = particleOffset;
            currentLife = particles[currentOffset] - 0.03f;
            if (currentLife < 0) {
                addParticleImpl(i, false);
                continue;
            }
            updateParticle(i);
        }
    }

    /**
     * 更新生命周期，更新透明度
     */
    private void updateParticle(int index) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        particles[currentOffset] = particles[currentOffset] - 0.03f;
        if (particles[currentOffset] < 0.3) {//准备消失
            particles[currentOffset + 1] = particles[currentOffset + 1] - 0.1f;
        } else {
            if (particles[currentOffset + 1] < 1) {
                particles[currentOffset + 1] = particles[currentOffset + 1] + 0.1f;
                particles[currentOffset + 1] = particles[currentOffset + 1] > 1f ? 1f : particles[currentOffset + 1];
            }
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 创建粒子，v字型散布
     */
    public void addParticles() {
        for (int i = 0; i < starNum; i++) {
            addParticleImpl(i, true);
        }
    }


    private void addParticleImpl(int index, boolean isInit) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        //记录生命周期时间
        particles[currentOffset++] = random.nextInt(3) + 1.5f;//时长
        if (isInit) {
            particles[currentOffset++] = random.nextFloat();//透明度
        } else {
            particles[currentOffset++] = 0;//透明度
        }
        Geometry.Point point = createStar();
        particles[currentOffset++] = point.x;
        particles[currentOffset++] = point.y;
        particles[currentOffset++] = 0;
        particles[currentOffset++] = evaluatePointSize();
        //增大随机数范围减少重复
        particles[currentOffset++] = random.nextInt(textureTypeCount*10)/10;
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    private int evaluatePointSize() {
        int a = random.nextInt(100);
        int b;
        if (a < 60) {
            b = ConstantMediaSize.getMinWH() / 10;
        } else if (a < 80) {
            b = ConstantMediaSize.getMinWH() / 3;
        } else {
            b = ConstantMediaSize.getMinWH() / 6;
        }
        return b;
    }

    /**
     * * -\sqrt{2\sqrt{0.16x^{2}}-x^{2}}-0.3
     * * \sqrt{\sqrt{2}-\sqrt{\left|2.5x\right|}}-0.3
     *
     * @return
     */
    private Geometry.Point createStar() {
        Geometry.Point point = new Geometry.Point();
        switch (starType) {
            case LINE:
                float x = random.nextFloat() - 0.5f;//-0.5 to 0.5
                float y = x;
                x = (-0.2f * random.nextFloat() + 0.1f) + x;
                y = (-0.2f * random.nextFloat() + 0.1f) + y;
                point.x = x;
                point.y = y;
                break;
            //x从-0.8到0.8
            case HEART:
                x = 1.8f * random.nextFloat() - 0.9f;
                if (Math.abs(x) < 0.6) {//扩大0.6-0.9边的权重
                    if (random.nextInt(2) == 0) {
                        float flag = random.nextInt(2) == 0 ? 1 : -1;
                        x = (float) (flag * 0.6 + 0.3 * random.nextFloat());
                    }
                }
                point.x = x;
                switch (ConstantMediaSize.ratioType) {
                    case _16_9:
                        point.x = x * 9f / 16f;
                        break;
                }
                if (random.nextInt(2) == 0) {
                    point.y = (float) (-1f * Math.sqrt(2 * Math.sqrt(0.16 * x * x) - x * x) - 0.3f);
                } else {
                    point.y = (float) (Math.sqrt(Math.sqrt(2) - Math.sqrt(Math.abs(2.5 * x))) - 0.3f);
                }
                switch (ConstantMediaSize.ratioType) {
                    case _1_1:
                        break;
                    case _4_3:
                        point.y = point.y * 4f / 3f;
                        break;
                    case _16_9:
//                        point.y = point.y * 9f / 16f;
                        break;
                    case _3_4:
                        point.y = point.y * 3f / 4f;
                        break;
                    case _9_16:
                        point.y = point.y * 9f / 16f;
                        break;
                }

                break;
            case V_SHADE:
                x = -2 * random.nextFloat() + 1;//-1到1
                y = x < 0 ? x + 0.5f : -x + 0.5f;
                x = (-0.2f * random.nextFloat() + 0.1f) + x;
                y = (-0.2f * random.nextFloat() + 0.1f) + y;
                point.x = x;
                point.y = y;
                break;

        }
        //位移和大小变化
        point.x = centerX + point.x * ratio;
        point.y = centerY + point.y * ratio;
        return point;
    }

    /**
     * 0.625是1/2的四次方
     * 总体方案，1:1的公式
     * -\sqrt{2\sqrt{0.16x^{2}}-x^{2}}-0.3
     * \sqrt{\sqrt{2}-\sqrt{\left|2.5x\right|}}-0.3
     *
     * @return
     */
    private Geometry.Point createHeart() {
        Geometry.Point point = new Geometry.Point();
        //除去不显示区域部分
        float x = 1.8f * random.nextFloat() - 0.9f;
        if (Math.abs(x) < 0.6) {//扩大0.6-0.9边的权重
            if (random.nextInt(2) == 0) {
                float flag = random.nextInt(2) == 0 ? 1 : -1;
                x = (float) (flag * 0.6 + 0.3 * random.nextFloat());
            }
        }
        point.x = x;
        //0.625是1/2的四次方
        float ratio = 1;
        switch (ConstantMediaSize.ratioType) {
            case _9_16:
                break;
        }
        if (random.nextInt(2) == 0) {
            point.y = (float) (-1f * Math.sqrt(2 * Math.sqrt(0.16 * x * x) - x * x) - 0.3f);
        } else {
            point.y = (float) (Math.sqrt(Math.sqrt(2) - Math.sqrt(Math.abs(2.5 * x))) - 0.3);
        }
        return point;
    }


    int textureTypeCount = 1;

    public void setTextureTypeCount(int textureTypeCount) {
        this.textureTypeCount = textureTypeCount;
    }

    public static final int LINE = 0;
    public static final int HEART = 1;
    public static final int V_SHADE = 2;

    /**
     * 设置位移（中心点）坐标
     * @param centerX x坐标
     * @param centerY y坐标
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * 设置大小比例
     * @param ratio size ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    /**
     * 图形移动
     * @param centerX 坐标x
     * @param centerY 坐标y
     * @param ratio 图形大小
     */
    public void setPositionParameter(float centerX, float centerY, float ratio) {
        float x,y;
        int cur;
        for (int i = 0; i < starNum; i++) {
            cur = i * TOTAL_COUNT;
            x = (particles[cur + 2] - this.centerX)/this.ratio;
            y = (particles[cur + 3] - this.centerY)/this.ratio;
            particles[cur + 2] = x * ratio + centerX;
            particles[cur + 3] = y * ratio + centerY;
        }
        this.ratio = ratio;
        this.centerX = centerX;
        this.centerY = centerY;
        vertexArray.updateBuffer(particles, 0, starNum * TOTAL_COUNT);
    }
}
