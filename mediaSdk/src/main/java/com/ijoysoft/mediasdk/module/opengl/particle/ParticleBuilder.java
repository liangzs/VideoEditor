package com.ijoysoft.mediasdk.module.opengl.particle;

import android.graphics.Bitmap;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleBuilder {
    public ParticleType particleType;
    /**
     * 粒子纹理
     */
    public List<Bitmap> listBitmaps;
    /**
     * 粒子数量
     * 或  随机最大粒子数量
     */
    public int particleCount;


    public BaseParticleSystem customSystom;//对于默认渲染不满足情况，设置自定义渲染
    public int randomPointSize;//粒子大小  random(randomPointSize)+minpointsize
    public int basePointSize;
    public int minPointSize;
    public float pointSizeDecrease = 0.03f;//递减值
    public float pointScalePeriod = 1;
    public float expanSpeedMulti = 1;
    public boolean pointScaleNodispear;
    public boolean pointScaleNoRandom;
    public boolean isSelfRotate;//自旋转
    public boolean isAlpha;
    public boolean isColor;//自定义渲染颜色
    public boolean isScale;//是否进出场缩放效果
    public boolean isAccelelateInVerse;
    //消失动画时间,设了这个值之后，粒子的生命周期长度设为2，
    // 一个保留粒子创建的生命时间,另外一个存储的消失时间
    public float dispearTime;
    public IParticle.IAddParticle iAddParticle;
    public IParticle.ICreateColor iCreateColor;
    public IParticle.ICreateColorMap iCreateColorMap;
    public IParticle.ICreateDirector iCreateDirector;
    public IParticle.ICreateLocation iCreateLocation;
    public IParticle.ICreatePointSize iCreatePointSize;
    public IParticle.ICreateAccelerate iCreateAccelerate;
    public IParticle.ICreateTexture iCreateTexture;
    public IParticle.ITextureFlip flipTexture;
    //固定粒子坐标
    public float[][] fixParticles;
    public int[] fixPointSize;

    //生命存活时间=randonLife+minLife
    public float randomLife;
    public float minLife;


    /**
     * 撞击屏幕边缘反弹
     *
     * @value true 粒子撞击屏幕边缘反弹
     * @value false 粒子离开屏幕后消失
     * @see RandomParticleSystem#setRebound(boolean)
     */
    private boolean rebound;

    /**
     * EXPAND 类型向中间缩
     */
    public boolean expandIn = false;


    /**
     * 粒子大小变化值
     */
    private float particleSizeDelta = 4f;

    /**
     * 粒子大小相对值
     */
    private float particleSizeRatio = 0.01f;

    /**
     * 图形大小比例
     *
     * @see StarSystem#setRatio(float)
     */
    private float graphSizeRatio = 1f;

    /**
     * 图形中心点x轴
     */
    private float graphCenterX = 0f;

    /**
     * 图形中心点y轴
     */
    private float graphCenterY = 0f;


    /**
     * 单张bitmap
     * {@link RotateImageDrawer#init(Bitmap)}
     */
    private Bitmap singleMiMap;

    /**
     * 随机最大透明度
     */
    private boolean randomAlpha;


    /**
     * y轴位置
     */
    private float yPosition;

    /**
     * 关闭随机大小
     */
    private boolean sizeFreeze;

    /**
     * 短周期烟花
     */
    private Float shortLife = null;

    /**
     * 更新粒子数组
     */
    MyMovingSystem.ParticlePositionUpdater positionUpdater;

    /**
     * 粒子起点定义
     */
    MyMovingSystem.ParticleStartPositionGenerator startPositionGenerator = null;

    /**
     * 在第一帧就加载所有粒子
     * 一般用于单个Action中的drawer
     */
    public boolean loadAllParticleInFirstFrame;


    /**
     * 粒子相对大小范围 [sizeFloatingStart, 1]
     */
    private float sizeFloatingStart = 4f / 6f;

    /**
     * 随机透明度起始值
     * alpha = [randomParticleAlphaStart, 1]
     */
    private float randomAlphaStart = 1f;

    /**
     * 用自己的生成器加载第一帧
     */
    private boolean loadFirstFrameByGenerator;

    /**
     * 关闭渐变
     *
     * @see RandomFadeSystem#setDisableFade(boolean)
     */
    private boolean disableFade;


    public ParticleBuilder() {

    }

    public ParticleBuilder(ParticleType particleType, List<Bitmap> listBitmaps) {
        this.particleType = particleType;
        this.listBitmaps = listBitmaps;
        this.particleCount = 5;
        if (listBitmaps != null && particleType == ParticleType.FIX_COORD) {
            isSelfRotate = true;
        }
        this.randomPointSize = 5;
        this.basePointSize = 20;
        this.minPointSize = 1;
        this.pointSizeDecrease = 0.3f;
    }

    public ParticleBuilder setParticleSizeRatio(float particleSizeRatio) {
        this.particleSizeRatio = particleSizeRatio;
        return this;
    }

    public ParticleBuilder setParticleType(ParticleType particleType) {
        this.particleType = particleType;
        return this;
    }

    public ParticleBuilder setListBitmaps(List<Bitmap> listBitmaps) {
        this.listBitmaps = listBitmaps;
        return this;
    }

    public ParticleBuilder setParticleCount(int particleCount) {
        this.particleCount = particleCount;
        return this;
    }

    public ParticleBuilder setCustomSystom(BaseParticleSystem customSystom) {
        this.customSystom = customSystom;
        return this;
    }

    public ParticleBuilder setSelfRotate(boolean selfRotate) {
        isSelfRotate = selfRotate;
        return this;
    }

    /**
     * {@link #setLoadAllParticleInFirstFrame 这只alpha}
     *
     * @param alpha
     * @return
     */
    public ParticleBuilder setAlpha(boolean alpha) {
        isAlpha = alpha;
        return this;
    }

    public ParticleBuilder setColor(boolean isColor, IParticle.ICreateColor createColor) {
        this.isColor = isColor;
        this.iCreateColor = createColor;
        return this;
    }

    public ParticleBuilder setColorMap(IParticle.ICreateColorMap map) {
        isColor = true;
        this.iCreateColorMap = map;
        return this;
    }

    public ParticleBuilder setColor(boolean isColor) {
        this.isColor = isColor;
        return this;
    }

    public ParticleBuilder setIDirectionAction(IParticle.ICreateDirector director) {
        this.iCreateDirector = director;
        return this;
    }

    public ParticleBuilder setILocationAction(IParticle.ICreateLocation location) {
        this.iCreateLocation = location;
        return this;
    }

    public ParticleBuilder setIPointAction(IParticle.ICreatePointSize pointSize) {
        this.iCreatePointSize = pointSize;
        return this;
    }

    public ParticleBuilder setIAccelerate(IParticle.ICreateAccelerate accelerate) {
        this.iCreateAccelerate = accelerate;
        return this;
    }

    public ParticleBuilder setITexture(IParticle.ICreateTexture createTexture) {
        this.iCreateTexture = createTexture;
        return this;
    }

    public ParticleBuilder setPointSize(int randomPointSize, int basePointSize) {
        this.randomPointSize = randomPointSize;
        this.basePointSize = basePointSize;
        return this;
    }

    public ParticleBuilder setMinPointSize(int minPointSize) {
        this.minPointSize = minPointSize;
        return this;
    }

    public ParticleBuilder setPointSizeDecrease(float pointSizeDecrease) {
        this.pointSizeDecrease = pointSizeDecrease;
        return this;
    }

    public ParticleBuilder setDispearTime(float dispearTime) {
        this.dispearTime = dispearTime;
        return this;
    }

    public ParticleBuilder setFixParticlesCoord(float[][] fixParticles) {
        this.fixParticles = fixParticles;
        if (fixParticles != null && particleType == ParticleType.FIX_COORD) {
            this.particleCount = fixParticles.length;
            isSelfRotate = true;
        }
        return this;
    }

    public ParticleBuilder setiAddParticle(IParticle.IAddParticle iAddParticle) {
        this.iAddParticle = iAddParticle;
        return this;
    }

    public ParticleBuilder setScale(boolean scale) {
        isScale = scale;
        return this;
    }

    public ParticleBuilder setFixPointSize(int[] fixPointSize) {
        this.fixPointSize = fixPointSize;
        return this;
    }

    public ParticleBuilder setFixPointSize(int randomPointSize, int[] fixPointSize) {
        this.randomPointSize = randomPointSize;
        this.fixPointSize = fixPointSize;
        return this;
    }

    public ParticleBuilder setLifeTime(float randomLife, float minLife) {
        this.randomLife = randomLife;
        this.minLife = minLife;
        return this;
    }


    public ParticleBuilder setParticleSizeDelta(float particleSizeDelta) {
        this.particleSizeDelta = particleSizeDelta;
        return this;
    }

    public ParticleBuilder setSingleMiMap(Bitmap singleMiMap) {
        this.singleMiMap = singleMiMap;
        return this;
    }


    public ParticleBuilder setRandomAlpha(boolean randomAlpha) {
        this.randomAlpha = randomAlpha;
        return this;
    }

    public ParticleBuilder setRandomAlphaStart(float randomAlphaStart) {
        this.randomAlphaStart = randomAlphaStart;
        return this;
    }

    public ParticleBuilder setyPosition(float yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    public ParticleBuilder setPositionUpdater(MyMovingSystem.ParticlePositionUpdater positionUpdater) {
        this.positionUpdater = positionUpdater;
        return this;
    }

    public ParticleBuilder setStartPositionGenerator(MyMovingSystem.ParticleStartPositionGenerator startPositionGenerator) {
        this.startPositionGenerator = startPositionGenerator;
        return this;
    }

    public ParticleBuilder setSizeFreeze(boolean sizeFreeze) {
        this.sizeFreeze = sizeFreeze;
        return this;
    }

    public ParticleBuilder setSizeFloatingStart(float sizeFloatingStart) {
        this.sizeFloatingStart = sizeFloatingStart;
        return this;
    }

    public ParticleBuilder setLoadAllParticleInFirstFrame(boolean loadAllParticleInFirstFrame) {
        this.loadAllParticleInFirstFrame = loadAllParticleInFirstFrame;
        return this;
    }

    public void setGraphSizeRatio(float graphSizeRatio) {
        this.graphSizeRatio = graphSizeRatio;
    }

    public void setGraphCenter(float graphCenterX, float graphCenterY) {
        this.graphCenterX = graphCenterX;
        this.graphCenterY = graphCenterY;
    }

    public ParticleBuilder setShortLife(Float shortLife) {
        this.shortLife = shortLife;
        return this;
    }

    public ParticleBuilder setExpandIn(boolean expandIn) {
        this.expandIn = expandIn;
        return this;
    }

    public ParticleBuilder setPointScalePeriod(float period) {
        this.pointScalePeriod = period;
        return this;
    }

    public ParticleBuilder setExpanSpeedMulti(float expanSpeedMulti) {
        this.expanSpeedMulti = expanSpeedMulti;
        return this;
    }

    public ParticleBuilder setPointScaleNodispear(boolean pointScaleNodispear) {
        this.pointScaleNodispear = pointScaleNodispear;
        return this;
    }

    public ParticleBuilder setPointScaleNoRandom(boolean pointScaleNoRandom) {
        this.pointScaleNoRandom = pointScaleNoRandom;
        return this;
    }

    /**
     * 撞击屏幕边缘反弹
     *
     * @value true 粒子撞击屏幕边缘反弹
     * @value false 粒子离开屏幕后消失
     * @see RandomParticleSystem#setRebound(boolean)
     */
    public ParticleBuilder setRebound(boolean rebound) {
        this.rebound = rebound;
        return this;
    }


    /**
     * 用自己的生成器加在第一帧
     *
     * @see MyMovingSystem#setLoadFirstFrameByGenerator(boolean)
     */
    public ParticleBuilder setLoadFirstFrameByGenerator(boolean loadFirstFrameByGenerator) {
        this.loadFirstFrameByGenerator = loadFirstFrameByGenerator;
        return this;
    }

    /**
     * 关闭渐变
     *
     * @see RandomFadeSystem#setDisableFade(boolean)
     */
    public ParticleBuilder setDisableFade(boolean disableFade) {
        this.disableFade = disableFade;
        return this;
    }

    /**
     * 翻转纹理，对于glpoint，顶点的翻转没有作用，所以在纹理的坐标进行翻转
     */
    public ParticleBuilder setFlipTexture(IParticle.ITextureFlip textureFlip) {
        this.flipTexture = textureFlip;
        return this;
    }

    /**
     * 生成粒子绘制器
     *
     * @return {@link IParticleRender} 粒子绘制器
     */
    public IParticleRender build() {
        switch (particleType) {
            case RANDOM_FADE:
                RandomFadeDrawer randomFadeDrawer = new RandomFadeDrawer();
                if (flipTexture != null) {
                    randomFadeDrawer.setFlip(flipTexture.isFLip());
                }
                RandomFadeSystem randomFadeSystem = new RandomFadeSystem(listBitmaps.size(), particleCount, particleSizeRatio);
                randomFadeSystem.setRandomAlpha(true);
                randomFadeSystem.setSizeFreeze(sizeFreeze);
                randomFadeSystem.setLoadAllParticleInFirstFrame(loadAllParticleInFirstFrame);
                randomFadeDrawer.setParticleSystem(randomFadeSystem);
                randomFadeDrawer.init(listBitmaps);
                randomFadeSystem.setDisableFade(disableFade);
                return randomFadeDrawer;
            case RANDOM:
                RandomParticlesDrawer randomParticlesDrawer = new RandomParticlesDrawer();
                if (flipTexture != null) {
                    randomParticlesDrawer.setFlip(flipTexture.isFLip());
                }
                RandomParticleSystem randomParticleSystem = new RandomParticleSystem(listBitmaps.size(), particleCount, particleSizeRatio);
                randomParticlesDrawer.setParticleSystem(randomParticleSystem);
                randomParticleSystem.setSizeFree(sizeFreeze);
                randomParticleSystem.setLoadAllParticleInFirstFrame(loadAllParticleInFirstFrame);
                randomParticleSystem.setSizeFloatingStart(sizeFloatingStart);
                randomParticleSystem.setRebound(rebound);
                randomParticleSystem.setColorCreator(iCreateColor);
                randomParticleSystem.setRandomAlphaStart(randomAlphaStart);
                randomParticlesDrawer.init(listBitmaps);
                return randomParticlesDrawer;
            case ROTATE_IMAGE:
                RotateImageDrawer rotateImageDrawer = new RotateImageDrawer();
                rotateImageDrawer.setParticleSystem(new RotateImageSystem(fixParticles, particleSizeDelta));
                rotateImageDrawer.init(singleMiMap);
                return rotateImageDrawer;
            case SCALE_FADE:
                ScaleFadeDrawer scaleFadeDrawer = new ScaleFadeDrawer();
                if (flipTexture != null) {
                    scaleFadeDrawer.setFlip(flipTexture.isFLip());
                }
                ScaleFadeSystem scaleFadeSystem = new ScaleFadeSystem(listBitmaps.size(), particleCount, particleSizeDelta);
                scaleFadeSystem.setLoadAllParticleInFirstFrame(loadAllParticleInFirstFrame);
                scaleFadeDrawer.setParticleSystem(scaleFadeSystem);
                scaleFadeSystem.setSizeFree(sizeFreeze);
                scaleFadeDrawer.init(listBitmaps);
                scaleFadeSystem.setShortLife(shortLife);
                return scaleFadeDrawer;
            case MOVING:
                MyMovingDrawer drawer = new MyMovingDrawer();
                if (flipTexture != null) {
                    drawer.setFlip(flipTexture.isFLip());
                }
                MyMovingSystem system = new MyMovingSystem(listBitmaps.size(), particleCount, particleSizeRatio);
                system.setPositionUpdater(positionUpdater);
                system.setSizeFreeze(sizeFreeze);
                system.setLoadAllParticleInFirstFrame(loadAllParticleInFirstFrame);
                drawer.setParticleSystem(system);
                drawer.init(listBitmaps);
                system.setColorCreator(iCreateColor);
                system.setSizeFloatingStart(sizeFloatingStart);
                system.setRandomAlphaStart(randomAlphaStart);
                if (positionUpdater instanceof MyMovingDrawer.ParticleUpdaterWithScreenRatio) {
                    drawer.setParticleUpdater((MyMovingDrawer.ParticleUpdaterWithScreenRatio) positionUpdater);
                }
                if (startPositionGenerator != null) {
                    system.setStartPositionGenerator(startPositionGenerator);
                }
                system.setLoadFirstFrameByGenerator(loadFirstFrameByGenerator);
                return drawer;
            case STAR_LINE:
                StarSystem starSystem = new StarSystem(StarSystem.LINE);
                StarDrawer starDrawer = new StarDrawer();
                starDrawer.setParticleSystem(starSystem);
                starDrawer.init(listBitmaps);
                starSystem.setCenter(graphCenterX, graphCenterY);
                starSystem.setRatio(graphSizeRatio);
                return starDrawer;
            case STAR_HEART:
                StarSystem starSystem2 = new StarSystem(StarSystem.HEART);
                StarDrawer starDrawer2 = new StarDrawer();
                starDrawer2.setParticleSystem(starSystem2);
                starDrawer2.init(listBitmaps);
                starSystem2.setCenter(graphCenterX, graphCenterY);
                starSystem2.setRatio(graphSizeRatio);
                return starDrawer2;
            case START_V_SHADE:
                StarSystem starSystem3 = new StarSystem(StarSystem.V_SHADE);
                StarDrawer starDrawer3 = new StarDrawer();
                starDrawer3.setParticleSystem(starSystem3);
                starDrawer3.init(listBitmaps);
                starSystem3.setCenter(graphCenterX, graphCenterY);
                starSystem3.setRatio(graphSizeRatio);
                return starDrawer3;
            default:
                return new ParticleDefaultDrawer(this);
        }


    }

    public IParticle.IAddParticle getiAddParticle() {
        return iAddParticle;
    }

    /**
     * 添加bitmap
     *
     * @param bitmap
     * @return
     */
    public ParticleBuilder addBitmap(Bitmap bitmap) {
        if (listBitmaps == null) {
            listBitmaps = new ArrayList<>();
        }
        listBitmaps.add(bitmap);
        singleMiMap = bitmap;
        return this;
    }


    /**
     * 获取线性落体位置更新器
     */
    public static MyMovingSystem.ParticlePositionUpdater createFallingUpdater() {
        return (particle, offset, length) ->
                particle[offset + 1] += 0.01f;
    }

    /**
     * 获取线性冒泡位置更新器
     */
    public static MyMovingSystem.ParticlePositionUpdater createBubblingUpdater() {
        return (particle, offset, length) ->
                particle[offset + 1] -= 0.01f;
    }

    /**
     * 随机顶部粒子生成器使用的随机对象
     */
    private static Random random = new Random();


    /**
     * 随机顶部粒子生成器
     */
    public static MyMovingSystem.ParticleStartPositionGenerator createTopParticleGenerator() {
        return new MyMovingSystem.ParticleStartPositionGenerator() {

            @Override
            public void setPosition(float[] particle, int offset) {
                particle[offset] = random.nextFloat() * 2f - 1f;
                particle[offset + 1] = -1f;
                particle[offset + 2] = 0f;
            }
        };
    }

    /**
     * 随机底部粒子生成器
     */
    public static MyMovingSystem.ParticleStartPositionGenerator createBottomParticleGenerator() {
        return new MyMovingSystem.ParticleStartPositionGenerator() {

            @Override
            public void setPosition(float[] particle, int offset) {
                particle[offset] = random.nextFloat() * 2f - 1f;
                particle[offset + 1] = 1f;
                particle[offset + 2] = 0f;
            }
        };
    }


}
