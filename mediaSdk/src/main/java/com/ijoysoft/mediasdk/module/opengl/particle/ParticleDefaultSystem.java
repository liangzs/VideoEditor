package com.ijoysoft.mediasdk.module.opengl.particle;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;

import static com.ijoysoft.mediasdk.module.opengl.particle.Geometry.BYTES_PER_FLOAT;

/**
 * 生命时间2，顶点3，方向3，颜色3，当前时间1，大小2，纹理类型1，透明度1：共：16个长度
 */
public class ParticleDefaultSystem extends BaseParticleSystem {
    //记录生命周期和消耗时间，在始末进行动画缩放大小，只做数据记录，不进行opengl数据的传导
    private static final int LIFETIME_COMPOMENT_COUNT = 2;
    //第一个值为使用值，第二值为最大值，第三个值为偏移值
    private static final int PARTICLE_POINT_SIZE_COUNT = 3;
    private static final int TETURE_COMPONENT_COUNT = 1;
    //给颜色添加一个索引，达到动态改变颜色的效果
    protected static final int COLOR_COMPONENT_COUNT = 5;
    private static final int PARTICLE_ALPHA_COUNT = 1;
    private static final int PARTICLE_ACCELERATE_COUNT = 1;//加速度方向问题
    //把能加的都加上去，用的值就取更新。不用到忽略
    public static final int TOTAL_COUNT =
            LIFETIME_COMPOMENT_COUNT +
                    PARTICLE_POINT_SIZE_COUNT +
                    POSITION_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT + TETURE_COMPONENT_COUNT + PARTICLE_ALPHA_COUNT + PARTICLE_ACCELERATE_COUNT;

    private static int LOCATION_INDEX = LIFETIME_COMPOMENT_COUNT +
            PARTICLE_POINT_SIZE_COUNT;
    private static int VECTOR_INDEX = LIFETIME_COMPOMENT_COUNT +
            PARTICLE_POINT_SIZE_COUNT +
            POSITION_COMPONENT_COUNT;
    private static int COLOR_INDEX = LIFETIME_COMPOMENT_COUNT +
            PARTICLE_POINT_SIZE_COUNT +
            POSITION_COMPONENT_COUNT
            + VECTOR_COMPONENT_COUNT;
    private static int TIME_INDEX = LIFETIME_COMPOMENT_COUNT +
            PARTICLE_POINT_SIZE_COUNT +
            POSITION_COMPONENT_COUNT
            + VECTOR_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT;
    private static final float LIFE_DECREASE = 1f / ConstantMediaSize.FPS;
    //出现和消失的时间点
    private static final float SHOW_TIME_POINT = 0.2f;
    private static final float ALPHA_DECREASE = 1f / (SHOW_TIME_POINT * ConstantMediaSize.FPS);

    protected static int STRIDE = TOTAL_COUNT * BYTES_PER_FLOAT;
    private ParticleBuilder builder;
    private float accelerateTime;//加速后时间
    private float currentLife;
    private float current;
    private float origin;
    private float temp;
    private int tempIndex;
    private float offtTime;
    private float currentX;
    private float currentY;

    public ParticleDefaultSystem(ParticleBuilder builder) {
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
                3, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
        dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;

        //这里是绑定不一样的纹理通道
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleTetureLocation(),
                TETURE_COMPONENT_COUNT, STRIDE);
        dataOffset += TETURE_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAlphaLocation(),
                PARTICLE_ALPHA_COUNT, STRIDE);
        dataOffset += PARTICLE_ALPHA_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getAccelerateLocation(),
                PARTICLE_ACCELERATE_COUNT, STRIDE);
    }


    public void addParticle(float particleStartTime) {
//        pointSize = ConstantMediaSize.getMinWH() / 60;
        if (builder.loadAllParticleInFirstFrame) {
            while (currentParticleCount < maxParticleCount) {
                addParticleImpl(currentParticleCount, particleStartTime);
                currentParticleCount++;
            }
        } else {
            if (currentParticleCount < maxParticleCount) {
                addParticleImpl(currentParticleCount, particleStartTime);
                currentParticleCount++;
            }
        }
        if (builder.particleType == ParticleType.FIX_COORD) {
            for (int i = 0; i < maxParticleCount; i++) {
                final int particleOffset = i * TOTAL_COUNT;
                if (particles[particleOffset + LIFETIME_COMPOMENT_COUNT] < particles[particleOffset + LIFETIME_COMPOMENT_COUNT + 1]) {
                    particles[particleOffset + LIFETIME_COMPOMENT_COUNT] = particles[particleOffset + LIFETIME_COMPOMENT_COUNT] +
                            particles[particleOffset + LIFETIME_COMPOMENT_COUNT + 1] / 10;
                    vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
                }
            }
            return;
        }
        updateParticle(particleStartTime);
    }

    /**
     * 生命周期递减值和帧率挂钩，位置点加上方向加值后的结果检测是否出边界
     *
     * @return
     */
    private void updateParticle(float particleStartTime) {
        for (int i = 0; i < maxParticleCount; i++) {
            int currentOffset = i * TOTAL_COUNT;
            if (builder.particleType != ParticleType.CENTER_EXPAND) {
                currentLife = particles[currentOffset] - LIFE_DECREASE;
                particles[currentOffset] = currentLife;
                if (currentLife < 0) {
                    addParticleImpl(i, particleStartTime);
                    continue;
                }
            }
            if (checkOutsideBound(currentOffset, particleStartTime)) {
                addParticleImpl(i, particleStartTime);
                continue;
            }
            checkUpdateParticleItem(i, particleStartTime);
        }
    }


    /**
     * 边界
     *
     * @param particleOffset
     * @param particleStartTime
     * @return
     */
    private boolean checkOutsideBound(int particleOffset, float particleStartTime) {
        if (builder.particleType == ParticleType.CENTER_EXPAND) {
            return false;
        }
        offtTime = particleStartTime - particles[particleOffset + TIME_INDEX];
        currentX = particles[particleOffset + LOCATION_INDEX] + particles[particleOffset + VECTOR_INDEX] * offtTime;
        currentY = particles[particleOffset + LOCATION_INDEX + 1] + particles[particleOffset + VECTOR_INDEX + 1] * offtTime;
        return currentX < -1.2 || 1.2 < currentX || currentY < -1.2 || 1.2 < currentY;
    }

    /**
     * 更新纹理
     *
     * @param particleOffset
     */
    private void updateFlyTexture(int particleOffset) {
        int particleIndex = particleOffset + TOTAL_COUNT - 2;
        float currentParticle = particles[particleIndex];
        particles[particleIndex] = (currentParticle + 1f) % builder.listBitmaps.size();
    }


    /**
     * 更新时间，更新大小
     */
    private void checkUpdateParticleItem(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        if (builder.isScale) {//缩放动作
            updateParticleScale(particleOffset);
        }
        if (builder.isAlpha) {
            updateParticleAplha(particleOffset);
        }
        if (builder.particleType == ParticleType.FLYING) {
            updateFlyTexture(particleOffset);
        }
        if (builder.particleType == ParticleType.SCALE_LOOP) {
            updateParticleScaleLoop(particleOffset, particleStartTime);
        }
        if (builder.particleType == ParticleType.CENTER_EXPAND) {
            updateParticleExpand(index, particleStartTime);
        }
        if (builder.particleType == ParticleType.EXPAND && builder.iCreateColorMap != null) {
            updateParticleColorMap(particleOffset);
        }
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }

    /**
     * 缩放操作
     *
     * @param currentOffset
     */
    private void updateParticleScale(int currentOffset) {
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset];
        if (current < SHOW_TIME_POINT) {//准备消失
            temp = particles[currentOffset + LIFETIME_COMPOMENT_COUNT] - particles[currentOffset + LIFETIME_COMPOMENT_COUNT + 1] / 4f;
            temp = temp < 0 ? 0 : temp;
            particles[currentOffset + LIFETIME_COMPOMENT_COUNT] = temp;
        }
        if (origin - current < SHOW_TIME_POINT) {
            current = particles[currentOffset + LIFETIME_COMPOMENT_COUNT] + particles[currentOffset + LIFETIME_COMPOMENT_COUNT + 1] / 4f;
            current = Math.min(current, particles[currentOffset + LIFETIME_COMPOMENT_COUNT + 1]);
            particles[currentOffset + LIFETIME_COMPOMENT_COUNT] = current;
        }
    }

    /**
     * 渐变生命起和终
     *
     * @param currentOffset
     */
    private void updateParticleAplha(int currentOffset) {
        tempIndex = currentOffset + TOTAL_COUNT - PARTICLE_ACCELERATE_COUNT - 1;
        current = particles[currentOffset];
        origin = particles[currentOffset + 1];
        current = particles[currentOffset];
        if (current < SHOW_TIME_POINT) {//准备消失
            temp = particles[tempIndex] - ALPHA_DECREASE;
            temp = temp < 0 ? 0 : temp;
            particles[tempIndex] = temp;
        }
        if (origin - current < SHOW_TIME_POINT) {
            current = particles[tempIndex] + ALPHA_DECREASE;
            current = Math.min(current, 1);
            particles[tempIndex] = current;
        }
    }

    /**
     * 缩放操作
     *
     * @param currentOffset
     */
    private void updateParticleScaleLoop(int currentOffset, float particleStartTime) {
        temp = (float) Math.abs(Math.sin(builder.pointScalePeriod * Math.PI * particleStartTime + particles[currentOffset + 4]));
        if (builder.pointScaleNodispear) {
            temp = temp * 0.3f + 0.7f;
        }
        particles[currentOffset + 2] = particles[currentOffset + 3] * temp;
    }

    /**
     * 缩放操作
     *
     * @param currentOffset
     */
    private void updateParticleColorMap(int currentOffset) {
        float[] colors = builder.iCreateColorMap.getColor((int) particles[currentOffset + COLOR_INDEX + 3],
                (int) particles[currentOffset + COLOR_INDEX + 4]);
        if (colors == null) {
            particles[currentOffset + COLOR_INDEX + 4] = particles[currentOffset + COLOR_INDEX + 4] - 1;
            return;
        }
        particles[currentOffset + COLOR_INDEX] = colors[0];
        particles[currentOffset + COLOR_INDEX + 1] = colors[1];
        particles[currentOffset + COLOR_INDEX + 2] = colors[2];
        particles[currentOffset + COLOR_INDEX + 3] = colors[3];
        particles[currentOffset + COLOR_INDEX + 4] = colors[4];
    }

    /**
     * 每间隔两秒扩散一秒时间操作，速度是3倍
     * 加速不应该加速系数，而是加速变量x，即时间
     */
    private void updateParticleExpandSpeed(ParticleShaderProgram particleProgram, float currentTime) {
        int remaind = (int) currentTime;
        //加速1.2f倍
        if (remaind % 3 == 2) {
            accelerateTime += 0.1 * builder.expanSpeedMulti;
        }
        particleProgram.setFloat(particleProgram.getuTimeLocation(), currentTime + accelerateTime);
    }


    /**
     * 更新位置
     */
    private void updateParticleExpand(int index, float currentTime) {
        float ratio = ((int) currentTime) % 3 == 2 ? 2f : 1f;
        ratio *= builder.expanSpeedMulti;
        int currentOffset = index * TOTAL_COUNT;
        float x = particles[currentOffset + LOCATION_INDEX];
        float y = particles[currentOffset + LOCATION_INDEX + 1];
        if (Math.abs(x) > Math.abs(y)) {
            y = y > 0 ? Math.abs(y / x) : -Math.abs(y / x);
            particles[currentOffset + LOCATION_INDEX] += 0.005f * ratio * (x < 0 ? -1 : 1);
            particles[currentOffset + LOCATION_INDEX + 1] += 0.005f * ratio * y;
        } else {
            x = x > 0 ? Math.abs(x / y) : -Math.abs(x / y);
            particles[currentOffset + LOCATION_INDEX] += 0.005f * ratio * x;
            particles[currentOffset + LOCATION_INDEX + 1] += 0.005f * ratio * (y < 0 ? -1 : 1);
        }

        if (particles[currentOffset + LOCATION_INDEX] < -1f || 1f < particles[currentOffset + LOCATION_INDEX] ||
                particles[currentOffset + LOCATION_INDEX + 1] < -1f || 1f < particles[currentOffset + LOCATION_INDEX + 1]) {
            LogUtils.v(getClass().getSimpleName(), "center expand particle out of screen, x: " + particles[currentOffset + LOCATION_INDEX] +
                    " y: " + particles[currentOffset + LOCATION_INDEX + 1]);
            addParticleImpl(index, currentTime);
        } else {
            LogUtils.v(getClass().getSimpleName(), "center expand particle position, x: " + particles[currentOffset + LOCATION_INDEX] +
                    " y: " + particles[currentOffset + LOCATION_INDEX + 1]);
        }
    }


    /**
     * 新增粒子
     *
     * @param index
     * @param particleStartTime
     */
    private void addParticleImpl(int index, float particleStartTime) {
        final int particleOffset = index * TOTAL_COUNT;
        if (builder != null && builder.getiAddParticle() != null) {
            builder.getiAddParticle().addParticle(particles, particleOffset, particleStartTime, builder);
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
            return;
        }

        int currentOffset = particleOffset;
        //记录生命周期时间
        temp = createDefaultLifeTime();
        if (builder.randomLife != 0) {
            temp = random.nextFloat() * builder.randomLife + builder.minLife;
        }
        particles[currentOffset++] = temp;//
        particles[currentOffset++] = temp;//1-4s生命周期
        temp = createDefaultPointSize(index);
        if (builder.iCreatePointSize != null) {
            temp = builder.iCreatePointSize.createPointSize();
        }
        if (builder.isScale || builder.particleType == ParticleType.SCALE_LOOP) {
            particles[currentOffset++] = 0;
        } else {
            particles[currentOffset++] = temp;//当前大小
        }
        //pointsize
        particles[currentOffset++] = temp;
        particles[currentOffset++] = builder.pointScaleNoRandom ? 0 : random.nextInt(10);
        //locaion
        Geometry.Point position = createDefaultLocation(index);
        if (builder.iCreateLocation != null) {
            float[] value = builder.iCreateLocation.createLocation(index);
            position = new Geometry.Point(value[0], value[1], value[2]);
        }
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;
        //方向随机,并且在glsl中取消重力加速度
        float[] director = createDefaultDirector(position);
        particles[currentOffset++] = director[0];
        particles[currentOffset++] = director[1];
        particles[currentOffset++] = director[2];
        float[] color = new float[3];
        if (builder.iCreateColor != null) {
            color = builder.iCreateColor.createColor();
        }
        if (builder.iCreateColorMap != null) {
            color = builder.iCreateColorMap.createColor();
        }
        /*颜色*/
        particles[currentOffset++] = color[0];
        particles[currentOffset++] = color[1];
        particles[currentOffset++] = color[2];
        //颜色索引
        particles[currentOffset++] = color.length > 4 ? color[3] : 0f;
        particles[currentOffset++] = color.length > 5 ? color[4] : 0f;
        //time
        particles[currentOffset++] = particleStartTime;
        //纹理
        particles[currentOffset] = random.nextInt(builder.listBitmaps.size());
        if (builder.iCreateTexture != null) {
            particles[currentOffset] = builder.iCreateTexture.createTexture(index);
        }
        currentOffset++;
        temp = createDefaultAlpha(position);
        particles[currentOffset++] = temp;
        temp = builder.isAccelelateInVerse ? -1f : 1f;
        if (builder.iCreateAccelerate != null) {
            temp = builder.iCreateAccelerate.createAccelerate();
        }
        particles[currentOffset++] = temp;
        LogUtils.v(getClass().getSimpleName(), "new Particle");
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COUNT);
    }


    /**
     * 创建默认的位置
     *
     * @return
     */
    private Geometry.Point createDefaultLocation(int index) {
        Geometry.Point position = null;
        switch (builder.particleType) {
            case HOVER:
                position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
                break;
            case FALLING:
                float y = -0.2f * random.nextFloat() - 1f;
                if (currentParticleCount < maxParticleCount) {//初始化阶段
                    y = random.nextFloat() * -2f + 0.9f;
                }
//                LogUtils.v("createDefaultLocation:", "y:" + y);
                position = new Geometry.Point(random.nextFloat() * -2f + 1f, y, 0f);
                break;
            case EXPAND:
                //半径值在0.1~0.2之间的值
                float b = random.nextFloat() * 0.03f + 0.01f;
                float x = (float) (random.nextFloat() * Math.sqrt(b));
                y = (float) Math.sqrt(b - x * x);
                position = new Geometry.Point(x * randomInverse(),
                        y * randomInverse(), 0f);
                break;
            case CENTER_EXPAND:
                if (currentParticleCount == maxParticleCount) {
                    double sita = random.nextFloat() * 2f * Math.PI;
                    position = new Geometry.Point((float) (0.05f * Math.cos(sita)), (float) (0.05f * Math.sin(sita)), 0f);
                } else if (builder.loadAllParticleInFirstFrame) {
                    position = new Geometry.Point(2f * (random.nextFloat() - 0.5f), 2f * (random.nextFloat() - 0.5f), 0f);
                } else {
                    position = new Geometry.Point(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, 0f);
                }
                //LogUtils.v("CENTER_ENPAND, new Particle", "x: " + position.x + " y:" + position.y);
                break;
            case FIX_COORD:
                position = new Geometry.Point(builder.fixParticles[index][0], builder.fixParticles[index][1], 0);
                break;
            case FLYING:
                position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
                break;
            case SCALE_LOOP:
                position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
                break;
            default:
                position = new Geometry.Point(random.nextFloat() * -2f + 1f, random.nextFloat() * -2f + 1f, 0f);
                break;
        }
        return position;
    }

    /**
     * 随机方向
     *
     * @return
     */
    private float randomInverse() {
        return random.nextInt(2) > 0 ? -1f : 1f;
    }

    /**
     * 默认方向
     *
     * @param location
     * @return
     */
    private float[] createDefaultDirector(Geometry.Point location) {
        float[] direction = new float[3];
        switch (builder.particleType) {
            case HOVER:
                //-0.1-0.1
                direction[0] = (random.nextFloat() - 0.5f) * 0.2f;
                direction[1] = (random.nextFloat() - 0.5f) * 0.2f;
                break;
            case FALLING:
                //下落块，横向慢
                float offset = random.nextInt(2) == 0 ? -1f : 1f;
                direction[0] = 0.05f * offset;
                direction[1] = 0.3f;
                break;
            case EXPAND:
                if (builder.expandIn) {
                    direction[0] = location.x * -0.2f * builder.expanSpeedMulti;
                    direction[1] = location.y * -0.2f * builder.expanSpeedMulti;
                } else {
                    direction[0] = location.x * 0.2f * builder.expanSpeedMulti;
                    direction[1] = location.y * 0.2f * builder.expanSpeedMulti;
                }
                break;
            case CENTER_EXPAND:
//                if (builder.expandIn) {
//                    direction[0] = location.x * -0.2f;
//                    direction[1] = location.y * -0.2f;
//                } else {
//                    direction[0] = location.x * 0.2f;
//                    direction[1] = location.y * 0.2f;
//                }
                break;
            case FIX_COORD:
                break;
            case FLYING:
                direction[0] = (random.nextFloat() - 0.5f) * 0.2f;
                direction[1] = (random.nextFloat() - 1f) * 0.5f;
                break;
        }
        return direction;
    }

    /**
     * 设置默认大小
     *
     * @param index
     * @return
     */
    private float createDefaultPointSize(int index) {
        float pointSize = (random.nextInt(builder.randomPointSize) + builder.minPointSize) * (ConstantMediaSize.getMinWH() / builder.basePointSize);
        if (builder.particleType == ParticleType.FIX_COORD) {
            return builder.fixPointSize[index];
        }
        return pointSize;
    }

    /**
     * 创建默认生命周期
     *
     * @return
     */
    private float createDefaultLifeTime() {
        switch (builder.particleType) {
            case HOVER:
                return random.nextInt(4) + 1;
            case FALLING:
                return random.nextInt(6) + 1;
            case EXPAND:
                return random.nextInt(4) + 2;
            case FLYING:
                return random.nextInt(6) + 1;
        }
        return random.nextInt(4) + 1;
    }

    /**
     * 创建默认透明度
     *
     * @return
     */
    private float createDefaultAlpha(Geometry.Point point) {
        float alpha = builder.isAlpha ? 0 : 1f;//alpha
        if (builder.particleType == ParticleType.FALLING && currentParticleCount < maxParticleCount) {
            alpha = 0;
            if (point.y < -1) {
                alpha = 1;
            }
        }
//        LogUtils.v("createDefaultLocation:", "alpha:" + alpha);
        return alpha;
    }

    @Override

    public void onDrawFrame(ParticleShaderProgram particleProgram, float currentTime) {
//        switch (builder.particleType) {
//            case EXPAND:
//                updateParticleExpand(particleProgram, currentTime);
//                break;
//        }
        if (!builder.listBitmaps.isEmpty()) {
            addParticle(currentTime);
            bindData(particleProgram);
            draw();
        }
    }

}
