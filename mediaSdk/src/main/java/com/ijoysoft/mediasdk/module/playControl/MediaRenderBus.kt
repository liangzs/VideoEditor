package com.ijoysoft.mediasdk.module.playControl

import android.graphics.Bitmap
import android.opengl.GLES20
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.*
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.InnerBorder
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle
import com.ijoysoft.mediasdk.module.opengl.particle.PAGOneBgParticle
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDrawerManager
import com.ijoysoft.mediasdk.module.opengl.theme.*
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeVideoExample
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter
import com.ijoysoft.mediasdk.view.RenderSingleType

/**
 * 渲染总线,从这里管控视频、图片的渲染层，
 * 以及主题，片段切换的过程管理
 * glsurfaceview和egl都直接使用其直接渲染
 * 只是glsurfaceview多了一个渲染的手动刷新,而egl不需要
 *
 *@since 2022年11月3日15:08:38
 * 迁移部分
 * mediapreview的mediarender部分
 * imageplayer的主题部分
 * mediadrawer 转场+粒子部分
 *
 * 如果是主题，时间管控用Image
 *
 */
class MediaRenderBus(val mediaCallback: IMediaCallback?) : IMediaRender, IThemeCallback {
    //视频、图片渲染层
    private var progressTrigger: ThemeProgressTrigger? = null
    //private var imageRender: IMediaRender? = null
    //private var videoRender: IMediaRender? = null


    //主题挂载
    var themeBackgroundManager: ThemeIRender? = null
        private set

    //zview轴主题用到
    private var themeNextFilter: IAction? = null

    //主题内部粒子系统
    private var particlesDrawer: ParticleDrawerManager? = null

    //渲染状态，默认主题分进场悬停和出长三个状态
    private var renderState: ActionStatus = ActionStatus.ENTER

    //主题类的控件资源
    private var widgetSource: List<Bitmap>? = null;

    @Volatile
    private var curIndex = 0

    //数据源
    private var mediaList: MutableList<MediaItem> = ArrayList()

    private var mediaConfig: MediaConfig? = null;

    //当前片段
    private var currentItem: MediaItem? = null

    //渲染任务，使其在opengl环境中运行
    private var queneTask: ((run: Runnable) -> Unit)? = null;

    //展示区域信息
    private var showWidth = 0;
    private var showHeight = 0;
    private var screenWidth = 0;
    private var screenHeight = 0;

    //是否预览转场
    private var isPreviewTransition: Boolean = false;


    /**
     * 创建缓存对象，离屏buffer
     * 0为前帧视频，1为后帧视频
     */
    private val fFrame = IntArray(1)
    private val fTextureSize = 2
    private val fTexture = IntArray(fTextureSize)

    var initCreate = false;

    //全局渲染，包含粒子，边框，pag粒子，以及基础转场和pag转场
    private var mOpenglDrawer: GlobalDrawer? = null
    override fun onSurfaceCreated() {
        progressTrigger?.onSurfaceCreated()
        mOpenglDrawer?.onSurfaceCreated()
        particlesDrawer = ThemeHelper.createParticles()
        particlesDrawer?.onSurfaceCreated()
        //放到这里创建对象，是因为有些手机页面切换会走这个过程，导致数据未同步
        if (mediaConfig!!.isTriming) {
            themeBackgroundManager = ThemeNoneManager()
        } else {
            themeBackgroundManager = ThemeHelper.createThemeManager()
        }

        //控件挂载时间轴
        if (isTimeTheme()) {
            (themeBackgroundManager as BaseTimeThemeManager).let {
                it.setWidgetMipmaps(widgetSource)
                //同时检测是否有视频片段，提前做好视频渲染的准备工作
                it.setExistVideo(checkExistVideo())
            }
        }
        themeBackgroundManager?.onSurfaceCreated()
        if (themeBackgroundManager is ThemeOpenglManager) {
            (themeBackgroundManager as ThemeOpenglManager).setIsPureColor(mediaConfig?.bgInfo)
        }
        initCreate = true;
    }

    override fun onSurfaceChanged(offsetX: Int, offsetY: Int, showWidth: Int, showHeight: Int, screenWidth: Int, screenHeight: Int) {
        this.showWidth = showWidth;
        this.showHeight = showHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        mOpenglDrawer?.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, screenWidth, screenHeight)
        progressTrigger?.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, screenWidth, screenHeight)
        mOpenglDrawer?.setThemeTransition(themeBackgroundManager?.themeTransition())
        checkInnerParticleContainPag()
        themeBackgroundManager?.onSurfaceChanged(offsetX, offsetY, showWidth, showHeight, screenWidth, screenHeight)
        themeBackgroundManager?.initBackgroundTexture()
        particlesDrawer?.onSurfaceChanged(0, 0, showWidth, showHeight, screenWidth, screenHeight)
        //创建离屏缓存
        createFBo(showWidth, showHeight)
        if (initCreate) {
            mediaPrepare()
        }
        currentItem?.let { mOpenglDrawer?.setMediaMatrix(it.mediaMatrix) }

        initCreate = false
    }

    override fun onDestroy() {
        progressTrigger?.onDestroy()
        mOpenglDrawer?.onDestroy()
        themeBackgroundManager?.onDestroy()
        particlesDrawer?.onDestroy()
        deleteFrameBuffer()
        progressTrigger = null
        themeBackgroundManager = null;
    }

    init {
        mOpenglDrawer = GlobalDrawer()
        progressTrigger = ThemeProgressTrigger()
        progressTrigger?.init(this, mediaCallback)
    }

    fun checkInnerParticleContainPag() {
        particlesDrawer?.listParicles.let {
            it?.forEach { particle ->
                if (PAGNoBgParticle::class.java.isAssignableFrom(particle.javaClass)) {
                    if (particle is PAGNoBgParticle) {
                        mOpenglDrawer?.setThemeParticlePag(particle)
                    }
                    if (particle is PAGOneBgParticle) {
                        mOpenglDrawer?.setThemeParticlePag(particle)
                    }
                }
            }
        }
    }

    /**
     * 创建离谱缓存
     */
    private fun createFBo(width: Int, height: Int) {
        deleteFrameBuffer()
        GLES20.glGenFramebuffers(1, fFrame, 0)
        genTextures(width, height)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    // 生成Textures
    private fun genTextures(width: Int, height: Int) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0)
        if (fTexture[0] === -1) {
            GLES20.glGenTextures(fTextureSize, fTexture, 0)
        }
        for (i in 0 until fTextureSize) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i])
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, null)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        }
    }

    private fun deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
        /* 9.0以上的手机问题，纹理不进行回收了！ */GLES20.glDeleteTextures(fTextureSize, fTexture, 0)
        for (i in 0 until fTextureSize) {
            fTexture[i] = -1
        }
    }

    override fun start(autoPlay: Boolean) {
        //如果是视频，则先进行初始化
        progressTrigger?.start(autoPlay)
        progressTrigger?.createVideoPlay(autoPlay)
    }

    /**
     * 检测合成是第一站素材是否为视频
     */
    fun checkVideoMurge() {
        progressTrigger?.createVideoPlay(true)
    }


    override fun pause() {
        progressTrigger?.pause()
    }

    override fun resume() {
        progressTrigger?.resume();
        //mOpenglDrawer?.setPreviewStatus(TransitionFilter.PreviewStatus.NORMAL)
    }


    /**
     * 更新资源
     */
    fun updateDataSource(dataSource: MutableList<MediaItem>) {
        this.mediaList = dataSource;
        progressTrigger?.setDataSource(dataSource, mediaConfig)

    }

    /**
     * 设置主元素资源
     */
    fun setDataSource(dataSource: MutableList<MediaItem>, doodleItems: List<DoodleItem>?, mediaConfig: MediaConfig) {
        this.mediaList = dataSource
        this.mediaConfig = mediaConfig;
        progressTrigger?.setDataSource(dataSource, mediaConfig)
        mOpenglDrawer?.setDoodle(doodleItems, false)
        mOpenglDrawer?.setInnerBorder(mediaConfig.getInnerBorder())
        mOpenglDrawer?.setGlobalParticles(mediaConfig.getGlobalParticles())
    }

    /**
     * 清理nextfilter渲染，mediaprepare的时候切换给currentRender
     */
    fun clearPreMediaPrepare() {
        if (themeNextFilter != null) {
            themeNextFilter?.onDestroy()
            themeNextFilter = null;
        }
        mediaPrepare()
    }

    /**
     * 片段切换时候的纹理准备工作
     * mediadrawerprepare
     * 当片段是图片时，采取thememanager本身的actionrender进行默认初始化
     * 如果是视频的话，videorender初始化完毕后，对thememanager的actionrender进行覆盖
     */
    fun mediaPrepare() {
        //由于进行动画时，纹理的加载耗时200ms，所以不重新取，直接取nextfilter中的纹理
        if (themeBackgroundManager is ThemeOpenglManager || isPreviewTransition) {
            try {
                themeBackgroundManager?.drawPrepare(mOpenglDrawer, currentItem, curIndex)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return
        }
        //设置前两张滤镜
        if (ThemeHelper.isChangeThemeReset(ConstantMediaSize.themeType)) {
            themeBackgroundManager?.drawPrepare(mOpenglDrawer, currentItem, curIndex)
            themeBackgroundManager?.setPreAfilter(mediaList, curIndex)
            return
        }
        zviewThemePrepare(curIndex)
        particlesDrawer?.onPlepare(curIndex)
    }

    /**
     *带zview轴主题的片段切换，需要同时存在两个ActionRender
     */
    private fun zviewThemePrepare(index: Int) {
        if (index > 0 && themeNextFilter != null && !(themeNextFilter is TimeVideoExample) && themeNextFilter!!.texture != -1) {
            themeBackgroundManager?.setActionRender(themeNextFilter)
        } else {
            themeBackgroundManager?.drawPrepare(mOpenglDrawer, currentItem, index)
        }
        if (index < mediaList.size - 1) {
            themeNextFilter = themeBackgroundManager?.getNextAction(mediaList.get(index + 1), index + 1)
            if (mediaList.get(index + 1).isVideo && themeNextFilter != null) {
                progressTrigger?.createNextVideoPlay(mediaList.get(index + 1) as VideoMediaItem)
            }
        }
    }

    /**
     * 设置控件资源
     */
    override fun setWidgetDataSource(list: MutableList<Bitmap>?) {
        //做一份索引变量
        widgetSource = list;
        progressTrigger?.setWidgetDataSource(widgetSource)
        if (themeBackgroundManager is BaseTimeThemeManager) {
            (themeBackgroundManager as BaseTimeThemeManager).setWidgetMipmaps(widgetSource)
        }
    }

    override fun onFinish() {
        //TODO 后续这里改成themebackground的相关行为
        //progressTrigger?.onFinish()
    }

    override fun getPreTexture(): Int {
        TODO("Not yet implemented")
    }

    override fun getNextTexture(): Int {
        TODO("Not yet implemented")
    }

    fun previewMediaItem(index: Int, isPreivew: Boolean) {
        curIndex = index;
        mOpenglDrawer?.setPreviewStatus(TransitionFilter.PreviewStatus.PREVIEW)
        switchPlayer(Triple(index, 0, isPreivew))
    }

    override fun previewMediaItem(index: Int) {
    }


    override fun previewAfilter() {
        if (themeBackgroundManager != null) {
            themeBackgroundManager?.previewAfilter(currentItem)
        }
    }

    /**
     * 正在切换media，进度返回0
     */
    @Volatile
    var switchingPlayer = false

    /**
     * 片段切换
     * first curindex
     * two curindex-progress
     * three isPreview
     */
    fun switchPlayer(triple: Triple<Int, Long, Boolean>) {
        this.curIndex = triple.first;
        currentItem = mediaList.get(curIndex)
        queneTask?.let {
            switchingPlayer = true
            it.invoke {
                progressTrigger?.switchPlayer(curIndex, triple.third)
                mOpenglDrawer?.drawPrepare(mediaList.get(curIndex), curIndex, triple.second)
                mediaPrepare()
                if (triple.third) {
                    themeBackgroundManager?.action?.drawFramePreview()
                    noTransitionDraw()
                }
                switchingPlayer = false
            }
        } ?: run {
            mOpenglDrawer?.drawPrepare(mediaList.get(curIndex), curIndex, triple.second)
            progressTrigger?.switchPlayer(curIndex, triple.third)
            mediaPrepare()
            if (triple.third) {
                themeBackgroundManager?.action?.drawFramePreview()
                noTransitionDraw()
            }
        }
    }

    /**
     *添加渲染任务
     */
    fun requestRender(run: Runnable) {
        queneTask?.invoke(run);
    }

    /**
     * 如果涉及到视频的时候，进行来回切换时，根据videoDecoder的解码状态，过滤不必要的重复动作
     *
     * 进行片段切换
     */
    override fun setSeekTo(i: Int, ti: Int, forceRender: Boolean) {
        curIndex = i;
        currentItem = mediaList.get(curIndex)
        progressTrigger?.setSeekTo(Pair(i, ti), Triple(forceRender, true, false), null)
    }

    fun setSeekTo(i: Int, ti: Int, forceRender: Boolean, callback: () -> Unit) {
        curIndex = i;
        currentItem = mediaList.get(curIndex)
        progressTrigger?.setSeekTo(Pair(i, ti), Triple(forceRender, true, false), callback)
    }

    /**
     * 补充seekSwit不重新走requestRender
     */
    fun setSeekTo(seekPair: Pair<Int, Int>, currentRender: Boolean) {
        curIndex = seekPair.first;
        currentItem = mediaList.get(curIndex)
        progressTrigger?.setSeekTo(seekPair, Triple(false, false, currentRender), null)
    }


    override fun seekToEnd() {
        progressTrigger?.seekToEnd()
    }

    /**
     * 检测两个是否是不同渲染器
     *
     * @param curent
     * @param last
     * @return
     */
    private fun checkDiffRender(curent: Int, last: Int): Boolean {
        if (curent >= mediaList.size || last >= mediaList.size) {
            return false
        }
        return if (mediaList.get(curent)?.getMediaType() != mediaList.get(last)?.getMediaType()) {
            true
        } else false
    }


    override fun setPureColor(bgInfo: BGInfo?) {
        setColorInfo(bgInfo);
        mediaPrepare()
    }

    /**
     * 颜色背景赋值
     * //TODO 这里需要一个
     */
    fun setColorInfo(bgInfo: BGInfo?) {
        if (themeBackgroundManager is ThemeOpenglManager) {
            (themeBackgroundManager as ThemeOpenglManager).setIsPureColor(bgInfo)
        }
        mediaPrepare()
    }

    override fun doMediaRotate() {
        // 更新转场和滤镜
        requestRender {
            mediaPrepare()
        }
    }


    override fun getCurPosition(): Int {
        return if (switchingPlayer) {
//            LogUtils.w("MediaRenderBus", "Switching player, origin progress： ${progressTrigger?.curPosition ?: 0}");
            0
        } else {
            progressTrigger?.curPosition ?: 0;
        }
    }

    /**
     * 设置滤镜强度,建议采用renderSingleList方案
     */
    override fun setFilterStrength(progress: Float, renderSingleList: MutableList<String>?) {
        requestRender {
            renderSingleList?.remove(RenderSingleType.FILTER)
        }
    }

    /**
     * 转场预览
     */
    fun previewTransition(lastIndex: Int) {
        val previewSeekBack = if (mediaList.get(lastIndex).finalDuration < ConstantMediaSize.TRANSITION_PREVIEW) mediaList.get(lastIndex).finalDuration / 2
        else mediaList.get(lastIndex).finalDuration - ConstantMediaSize.TRANSITION_PREVIEW.toLong()
        progressTrigger?.previewTransition(true)
        setSeekTo(lastIndex, previewSeekBack.toInt(), true)
        progressTrigger?.resume()
    }

    override fun removeTransition() {
        progressTrigger?.removeTransition()
    }

    //TODO 视频缩放方案的替换，替换为图片背景图的缩放方案
    override fun setScaleTranlation(scale: Float, x: Int, y: Int, originX: Int, originY: Int) {
        if (currentItem?.getMediaMatrix() == null) {
            currentItem?.setMediaMatrix(MediaMatrix(scale, x, y, originX, originY))
        } else {
            currentItem!!.getMediaMatrix().setScale(scale)
            currentItem!!.getMediaMatrix().setOffsetX(x)
            currentItem!!.getMediaMatrix().setOffsetY(y)
            currentItem!!.getMediaMatrix().setOriginX(originX)
            currentItem!!.getMediaMatrix().setOriginY(originY)
        }
        requestRender {
            mediaPrepare()
            mediaCallback?.render()
        }
    }

    /**
     * 检查主题是否为opengl主题，可以改变片段时间
     */
    override fun checkSupportTransition(): Boolean {
        return themeBackgroundManager is ThemeOpenglManager
    }


    /**
     * 渲染刷新,挂载主题显示
     * @param renderState 如果status是Enter状态，则返回两个纹理于mediadraw进行纹理显示
     * 不然就返回当前渲染纹理
     */
    override fun onDrawFrame(position: Int) {
        // 黑色背景
        GLES20.glViewport(0, 0, screenWidth, screenHeight)
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // 显示区背景
        GLES20.glViewport(0, 0, showWidth, showHeight)
        //currenrender只做视频源的拉取
        /**
         * 主题挂载渲染
         */
        renderState = onDrawTheme(position);
        mOpenglDrawer?.setInputTexture(fTexture.get(0))
        mOpenglDrawer?.onDrawFrame(renderState, position)

    }

    /**
     * 主题以及默认渲染过程
     */
    private fun onDrawTheme(position: Int): ActionStatus {
        if (ObjectUtils.isEmpty(themeBackgroundManager?.action)) {
            return ActionStatus.STAY
        }
        themeBackgroundManager?.drawVideoFrame(progressTrigger?.videoPlayer?.getOutTexture()
            ?: 0)
        progressTrigger?.onDrawFrame()
        //--①--跟②同步
        if (themeNextFilter != null && themeBackgroundManager?.getAction()?.getStatus() == ActionStatus.OUT) {
            themeNextFilter?.drawVideoFrame(progressTrigger?.nextVideoPlayer?.getOutTexture()
                ?: 0)
        }
        //如果是时间轴主题，就直接绘画
        if (isTimeTheme()) {
            EasyGlUtils.bindFrameTexture(fFrame.get(0), fTexture.get(0))
            (themeBackgroundManager as BaseTimeThemeManager).onDrawFrame(position)
            particlesDrawer?.onDrawFrame()
            EasyGlUtils.unBindFrameBuffer()
            return themeBackgroundManager?.getAction()?.status ?: ActionStatus.STAY
        }
        EasyGlUtils.bindFrameTexture(fFrame.get(0), fTexture.get(0))
        themeBackgroundManager?.onDrawFrame()
        //兼容有些主题
        themeBackgroundManager?.onDrawFrame(position)
        //--②--跟①同步
        if (themeNextFilter != null && themeBackgroundManager?.getAction()?.getStatus() == ActionStatus.OUT) {
            //TODO 后续是否做两个视频做转场切换时，不做最后临界点的切换，而是做两个视频同时展示的切换
            progressTrigger?.ondrawNextFrame()
            themeNextFilter?.drawFrame()
        }
        themeBackgroundManager?.draFrameExtra()
        particlesDrawer?.onDrawFrame()
        EasyGlUtils.unBindFrameBuffer()

        //非opengl转场无转场动作，通过themeNextFilter完成转场
        if (themeBackgroundManager is ThemeOpenglManager) {
            return themeBackgroundManager!!.getActionStatus()
        }
        return ActionStatus.STAY
    }

    /**
     * 定位当前render
     */
    fun locationRender(mediaitem: MediaItem?) {
        currentItem = mediaitem;
        mOpenglDrawer?.setMediaMatrix(currentItem?.getMediaMatrix())
        curIndex = mediaList.indexOf(currentItem);
        progressTrigger?.setCurrentMediaItem(currentItem)
    }


    /**
     * 播放结束时候，对资源进行重置等
     */
    fun onFinishResetByProgress(progress: Int) {
        progressTrigger?.seekToEnd()
        progressTrigger?.setSeekTo(Pair(0, progress), Triple(true, false, false), null)
        curIndex = 0
        currentItem = mediaList.get(0)
        mOpenglDrawer?.resetIndex()
    }

    /**
     * 反序播放
     */
    fun videoReveasePlay(flag: Boolean) {
        progressTrigger?.setReverse(flag)
    }

    /**
     * 设置渲染任务
     */
    fun setRenderTask(task: ((run: Runnable) -> Unit)?) {
        this.queneTask = task;
    }

    /**
     * 是否有视频元素
     *
     * @return
     */
    fun existVideo(): Boolean {
        if (ObjectUtils.isEmpty(mediaList)) {
            return false
        }
        for (mediaItem in mediaList) {
            if (mediaItem?.isVideo) {
                return true
            }
        }
        return false
    }

    /**
     * 无转场内容渲染一帧
     */
    fun noTransitionDraw() {
        mOpenglDrawer?.setPreviewStatus(TransitionFilter.PreviewStatus.NONE)
        mediaCallback?.render()
    }

    /**
     * 设置文字等涂鸦信息
     */
    fun setDoodle(doodleItems: List<DoodleItem>) {
        mOpenglDrawer?.setDoodle(doodleItems, false)
    }

    /**
     *全屏展示的时候，会强制旋转屏幕
     */
    fun preViewRotate(flag: Boolean) {
        mOpenglDrawer?.preViewRotate(flag)
    }

    /**
     * 清理下一个片段
     */
    fun destoryThemeNext() {
        if (themeNextFilter != null) {
            themeNextFilter?.onDestroy()
            themeNextFilter = null
            progressTrigger?.nextVideoPlayer?.release()
            progressTrigger?.nextVideoPlayer = null;
        }
    }

    /**
     * 改变主题
     */
    override fun changeTheme(mediaConfig: MediaConfig?) {
        themeBackgroundManager?.onDestroy()
        particlesDrawer?.onDestroy()
        destoryThemeNext()
        createFBo(showWidth, showHeight)
        themeBackgroundManager = ThemeHelper.createThemeManager()
        //控件挂载时间轴
        if (isTimeTheme()) {
            (themeBackgroundManager as BaseTimeThemeManager).setWidgetMipmaps(widgetSource)
        }
        themeBackgroundManager?.onSurfaceCreated()
        themeBackgroundManager?.onSurfaceChanged(0, 0, showWidth, showHeight, screenWidth, screenHeight)
        themeBackgroundManager?.initBackgroundTexture()
        if (themeBackgroundManager is ThemeOpenglManager) {
            (themeBackgroundManager as ThemeOpenglManager).setIsPureColor(mediaConfig?.bgInfo)
        }
        mOpenglDrawer?.resetIndex();
        mOpenglDrawer?.setThemeTransition(themeBackgroundManager?.themeTransition())
        particlesDrawer = ThemeHelper.createParticles()
        particlesDrawer?.onSurfaceCreated()
        checkInnerParticleContainPag()
        particlesDrawer?.onSurfaceChanged(0, 0, showWidth, showHeight, screenWidth, screenHeight)
        //重新初始化片段
        seekToEnd()
        setSeekTo(Pair(0, 0), true)
        mediaPrepare()
    }

    /**
     * 设置视频音量,只针对视频
     */
    fun setVideoVolume(volume: Float) {
        progressTrigger?.setVolume(volume)
    }


    override fun setvideoSpeed(speed: Float) {
        progressTrigger?.setvideoSpeed(speed)
    }

    /**
     * 全局粒子系统
     */
    fun changeGlobalParticle(manager: ParticleDrawerManager?, pagNoBgParticle: PAGNoBgParticle?) {
        mOpenglDrawer?.setParticlesDrawerWhenPlaying(manager, pagNoBgParticle)
    }

    fun setGlobalParticles(globalParticles: GlobalParticles?) {
        mOpenglDrawer?.setGlobalParticles(globalParticles);
    }


    fun setInnerBorder(innerBorder: InnerBorder?) {
        mOpenglDrawer?.setInnerBorder(innerBorder)
    }

    /**
     * 全局边框
     */
    fun changeInnerBorder(innerBorder: InnerBorder) {
        mOpenglDrawer?.setInnerBorderWhenPlaying(innerBorder)
        mOpenglDrawer?.setPreviewStatus(TransitionFilter.PreviewStatus.NONE)
    }

    /**
     * 显示展示区大小
     */
    override fun setZoomScale(bgInfo: BGInfo?) {
        if (themeBackgroundManager is ThemeOpenglManager) {
            (themeBackgroundManager as ThemeOpenglManager).setIsPureColor(bgInfo)
        }
        mediaPrepare()
        mOpenglDrawer?.setPreviewStatus(TransitionFilter.PreviewStatus.NONE)
    }

    /**
     * 是否为时间轴主题
     */
    public fun isTimeTheme(): Boolean {
        return themeBackgroundManager is BaseTimeThemeManager
    }

    /**
     * 分辨率改变，需要重新加载比例资源
     * mediaItem中挂载的mipbitmaps要重现提换加载
     * 背景资源需要重新提换
     * 对于有些特殊的
     */
    fun updateRatio() {
        destoryThemeNext()
        mediaPrepare()
        //恢复原来进度
        //var tempDuration: Int = if (curIndex == 0) currentDuration else currentDuration + ConstantMediaSize.END_OFFSET
        //themeBackgroundManager?.seekTo(tempDuration)
        ////检测当前片段是否是出场状态，如果是出场状态，则再进行计算下一片段的进场时间
        //if (themeNextFilter != null && themeBackgroundManager?.action.status === ActionStatus.OUT) {
        //    tempDuration = (currentMediaItem.getDuration() - currentDuration).toInt()
        //    themeNextFilter.seek(tempDuration)
        //}
    }


    /**
     * 重新刷新当前片段展示效果
     */
    override fun imagePreparePreView() {
        mediaPrepare()
        themeBackgroundManager?.previewAfilter(currentItem)
        mediaCallback?.render()
    }

    /**
     * 重新设置背景图
     */
    override fun updateBgInfo(bgInfo: BGInfo?) {
        (themeBackgroundManager as ThemeOpenglManager).setIsPureColor(bgInfo)
        mediaPrepare()
    }

    /**
     * seekto操作
     *  @param seekPair first:seekIndex second:progress
     * @param renderPair first:forceRender  second:渲染一次   third:requestRender环境
     */
    override fun seekToImpl(seekPair: Pair<Int, Int>, renderPair: Triple<Boolean, Boolean, Boolean>) {
        if (curIndex != seekPair.first || renderPair.first) {
            curIndex = seekPair.first
            currentItem = mediaList.get(curIndex)
            //先清理
            if (themeBackgroundManager != null) {
                themeBackgroundManager?.onDestroyFragment()
            }
            destoryThemeNext()
            //重新创建
            mediaPrepare()
            themeBackgroundManager?.initBackgroundTexture()
            if (renderPair.first) {
                var innerDuration: Int = seekPair.second
                for (mediaItem in mediaList) {
                    innerDuration -= if (mediaItem.finalDuration != 0L && mediaItem.finalDuration <= innerDuration) {
                        mediaItem.finalDuration.toInt()
                    } else if (mediaItem.finalDuration <= innerDuration) {
                        mediaItem.finalDuration.toInt()
                    } else {
                        break
                    }
                }
                if (isTimeTheme()) {
                    themeBackgroundManager?.draFrameExtra()
                    (themeBackgroundManager?.action as BaseTimeThemeExample).drawCertainTimePreview(innerDuration)
                } else {
                    themeBackgroundManager?.action?.drawFramePreview()
                }
            }
            seekTheme(seekPair, renderPair)
            if (renderPair.second) {
                mediaCallback?.render()
            }
        }
    }

    /**
     * 主题seek
     *  @param seekPair first:seekIndex second:progress
     * @param renderPair first:forceRender  second:渲染一次   third:requestRender环境
     */
    override fun seekTheme(seekPair: Pair<Int, Int>, renderPair: Triple<Boolean, Boolean, Boolean>) {
        var tempDuration = if (seekPair.first == 0) seekPair.second else seekPair.second + ConstantMediaSize.themeType.endOffset
        themeBackgroundManager?.seekTo(tempDuration)
        //检测当前片段是否是出场状态，如果是出场状态，则再进行计算下一片段的进场时间
        if (themeNextFilter != null && themeBackgroundManager?.action?.status === ActionStatus.OUT) {
            tempDuration = (currentItem!!.finalDuration - seekPair.second).toInt()
            themeNextFilter?.seek(tempDuration)
        }
    }

    /**
     * 是否存在视频
     */
    fun checkExistVideo(): Boolean {
        for (i in 0 until mediaList.size) {
            if (mediaList.get(i).isVideo) {
                return true;
            }
        }
        return false;
    }

    override fun setCurrentMurPts(pts: Long?) {
        progressTrigger?.setCurrentMurPts(pts)
    }

    fun setIsMurging(isMurging: Boolean) {
        progressTrigger?.isMurging = isMurging;
    }


}