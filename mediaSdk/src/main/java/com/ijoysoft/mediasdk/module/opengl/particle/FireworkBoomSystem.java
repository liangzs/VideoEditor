package com.ijoysoft.mediasdk.module.opengl.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 就只走一个生命周期时间
 * 生命时间2，顶点3，方向3，颜色3，当前时间1，大小2，纹理类型1，透明度1：共：16个长度
 */
public class FireworkBoomSystem extends BaseParticleSystem {
    //记录生命周期和消耗时间，在始末进行动画缩放大小，只做数据记录，不进行opengl数据的传导
    private static final int LIFETIME_COMPOMENT_COUNT = 2;
    private static final int PARTICLE_POINT_SIZE_COUNT = 2;
    private static final int TETURE_COMPONENT_COUNT = 1;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    //把能加的都加上去，用的值就取更新。不用到忽略
    public static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    PARTICLE_POINT_SIZE_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + TETURE_COMPONENT_COUNT + PARTICLE_ALPHA_COUNT;

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private ParticleBuilder builder;
    //从这中心点进行散播
    private float centerX, centerY;

    public FireworkBoomSystem(ParticleBuilder builder) {
        this.builder = builder;
        maxParticleCount = builder.particleCount;
        particles = new float[maxParticleCount * TOTAL_COUNT];
        vertexArray = new ParticleUtil(particles);
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
        while (currentParticleCount < maxParticleCount) {
            addParticleImpl(currentParticleCount, particleStartTime);
            currentParticleCount++;
            return;
        }
        //更新透明度
        float alpha;
        for (int i = 1; i < maxParticleCount; i++) {
            alpha = particles[i * TOTAL_COUNT - 1];
            if (alpha == 1) {
                continue;
            }
            alpha = alpha + 0.3f;
            alpha = alpha > 1 ? 1 : alpha;
            particles[i * TOTAL_COUNT - 1] = alpha;
            vertexArray.updateBuffer(particles, i * TOTAL_COUNT, TOTAL_COUNT);
        }
    }


    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime) {
        addParticleExpand(index, particleStartTime);
    }

    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * 从中间散发
     */
    private void addParticleExpand(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        int currentOffset = particleOffset;
        //记录生命周期时间
        float temp = random.nextInt(4) + 2;
        if (builder.randomLife != 0) {
            temp = random.nextFloat() * builder.randomLife + builder.minLife;
        }
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期
        //pointsize
        float pointSize = (random.nextInt(builder.randomPointSize) + builder.minPointSize) * (ConstantMediaSize.getMinWH() / builder.basePointSize);
        if (builder.isScale) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = pointSize;//当前大小
        }
        particles[currentOffset++] = pointSize;
        int texture = random.nextInt(builder.listBitmaps.size());
        float offsetX = createLocation(texture);
        float offsetY = createLocation(texture);
        Geometry.Point position = new Geometry.Point(centerX + offsetX, centerY + offsetY, 1);
        LogUtils.v("", "Point:" + position.x + "," + position.y);
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //方向随机,并且在glsl中取消重力加速度
        float flag = position.x > centerX ? 1f : -1f;
        float value = flag * (1 + offsetX);
        particles[currentOffset++] = value - centerX;
        flag = position.y > centerY ? 1f : -1f;
        value = flag * (1 + offsetY);
        particles[currentOffset++] = value - centerY;
        particles[currentOffset++] = 0f;
        float[] color = new float[3];
        if (builder.iCreateColor != null) {
            color = builder.iCreateColor.createColor();
        }
        /*颜色*/
        particles[currentOffset++] = color[0];
        particles[currentOffset++] = color[1];
        particles[currentOffset++] = color[2];
        //time
        particles[currentOffset++] = particleStartTime;
        //纹理
        particles[currentOffset++] = random.nextInt(builder.listBitmaps.size());//randomTextureType();
        particles[currentOffset++] = builder.isAlpha ? 0f : 1f;//alpha
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 根据纹理类型，机型概率散播，大的纹理在外部，小的在内部概率
     */
    private float createLocation(int teture) {
        return random.nextFloat() * 0.2f - 0.1f;
//        float offset;
//        switch (teture) {
//            case 0:
//            case 1:
//            case 2:
//                //-0.3-0.3
//                offset = random.nextFloat() * 0.6f - 0.3f;
//                break;
//            case 3:
//            case 4:
//            case 5:
//                //0.2-0.4  -0.2- -0.4
//                float flag = random.nextInt(2) == 0 ? -1f : 1f;
//                offset = (random.nextFloat() * 0.2f + 0.2f) * flag;
//                break;
//            case 6:
//                //这个两个放到最外围 0.3-0.6
//                flag = random.nextInt(2) == 0 ? -1f : 1f;
//                offset = (random.nextFloat() * 0.3f + 0.3f) * flag;
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + teture);
//        }
//        return offset;
    }

    /**
     * 第一种占比60
     * 第二种 30
     * 第三种10
     *
     * @return
     */
    private int randomTextureType() {
        int a = random.nextInt(100);
        if (a < 60) { //012
            return random.nextInt(3);
        } else if (a < 90) {
            //345
            return random.nextInt(3) + 3;
        } else {
            //6
            return 6;
        }
    }


    public void reset() {
        currentParticleCount = 0;
    }

    @Override

    public void onDrawFrame(ParticleShaderProgram particleProgram, float currentTime) {
        addParticle(currentTime);
        bindData(particleProgram);
        draw();
    }

}
