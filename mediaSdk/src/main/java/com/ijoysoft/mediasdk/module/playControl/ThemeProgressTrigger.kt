package com.ijoysoft.mediasdk.module.playControl

import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.entity.BGInfo
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecode
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr.AudioDecodeListener
import com.ijoysoft.mediasdk.module.mediacodec.decode.MediaCodecPlayer
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum
import com.ijoysoft.mediasdk.view.RenderSingleType

/**
 * 主题进度触发器,如果涉及主题，则根据主题设置的默认时间进行触发，
 * 主题有一个针对视频的最大悬停时间设定，过了时间就进行暂停和取消
 *
 *
 * 当无主题时，根据照片的时长进行触发，视频根据视频本身进行触发
 */
class ThemeProgressTrigger : IMediaRender, IDecodeStateListenr {
    private val TAG = "ThemeProgressTrigger";
    private var mMediaItems: List<MediaItem>? = null

    //拿到渲染对象，必要时进行主动渲染
    private var currentMediaItem: MediaItem? = null
    private var currentIndex = 0
    private var mIMediaCallback: IMediaCallback? = null

    //转场预览的时间，根据部分pag转场动态拓展
    private var transiPreTime = 0

    //是否转场预览
    private var isPreviewTransition = false
    private var isTransitioning = false;

    //是否正在运行
    private var isPlaying = false

    //渲染总线
    private var BUS: MediaRenderBus? = null

    /*【theme-image part】*/ /*自定义刷新频率*/
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null
    private var itemDuration: Long = 0

    //当前片段的进度
    private var currentProgress: Long = 0
    private var renderTaskfSeek = ArrayDeque<String>(3)
    private var mediaConfig: MediaConfig? = null;

    /*【video part】*/
    //当前主渲染视频,通过videoList切换解码视频
    var videoPlayer: MediaCodecPlayer? = null

    /**
     *  当前主渲染视频,只维持一个渲染推动(目前需要手动进行requestRender渲染)：setOnFrameAvailableListener
     *  目前转移到了handlerThread来进行触发
     */
    //private var currentSurfaceTexture: SurfaceTexture? = null

    //主题片段进行切换时，下一个片段是视频片段
    var nextVideoPlayer: MediaCodecPlayer? = null;

    var isMurging: Boolean = false;

    override fun onSurfaceCreated() {
    }

    override fun onSurfaceChanged(offsetX: Int, offsetY: Int, width: Int, height: Int, screenWidth: Int, screenHeight: Int) {
    }


    override fun init(bus: MediaRenderBus, callback: IMediaCallback?) {
        BUS = bus
        mIMediaCallback = callback
        handlerThread = HandlerThread("imagePlayer")
        handlerThread!!.start()
        handler = object : Handler(handlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    RENDER_CODE -> startRender()
                }
            }
        }
    }

    /**
     * 无主题时，视频自主播放
     * 有主题时清空回调，走主题时间管理的渲染触发
     */
    private fun videoNoThemePlay() {
        //currentSurfaceTexture?.setOnFrameAvailableListener { surfaceTexture: SurfaceTexture? -> mIMediaCallback?.render() }
    }

    /**
     * 渲染
     */
    override fun onDrawFrame() {
        //LogUtils.v("render", "onDrawFrame..." + videoPlayer?.getOutTexture());
        videoPlayer?.onDrawFrame()
    }

    /**
     * 下一片段的绘画
     */
    fun ondrawNextFrame() {
        nextVideoPlayer?.checkPlayer()
        nextVideoPlayer?.onDrawFrame()
        LogUtils.v("render", "ondrawNextFrame..." + nextVideoPlayer?.getOutTexture());
    }

    /**
     * 如果是视频文件
     */
    fun videoPlayInterval(): Long {
        if (currentMediaItem is PhotoMediaItem) {
            return ConstantMediaSize.PHTOTO_FPS_TIME.toLong();
        }
        currentMediaItem?.let {
            if (it.speed != 0f && it.speed != 1f) {
                return (ConstantMediaSize.PHTOTO_FPS_TIME / it.speed).toLong()
            }
        }
        return ConstantMediaSize.PHTOTO_FPS_TIME.toLong();
    }

    override fun onFinish() {
        currentProgress = 0;
    }

    /**
     * 开始渲染
     */
    fun startRender() {
        if (!isPlaying || isMurging) {
            LogUtils.v("render", ":startRender-isPlaying$isPlaying,ConstantMediaSize.PHTOTO_FPS_TIME:" + ConstantMediaSize.PHTOTO_FPS_TIME)
            return
        }
        currentProgress += videoPlayInterval()
        transiPreTime = if (currentMediaItem!!.pagDuration != 0L) currentMediaItem!!.pagDuration.toInt() else ConstantMediaSize.TRANSITION_DURATION
        //LogUtils.v("render", ":currentProgress:" + currentProgress + ",transiPreTime," + transiPreTime + " itemDuration:" + itemDuration)
        if (isTransitioning && currentProgress > transiPreTime) {
            isTransitioning = false
            pause()
            mIMediaCallback!!.onPause()
            return
        }
        calcDuration()
        if (currentProgress > itemDuration) {
            mIMediaCallback!!.render()
            mIMediaCallback!!.onComplet()
            isPlaying = false
            return
        }
        mIMediaCallback?.render()
        handler!!.sendMessageDelayed(handler!!.obtainMessage(RENDER_CODE), videoPlayInterval())
    }

    /**
     * 计算当前片段的剩余时间
     */
    private fun calcDuration() {
        //修复时长错误
        itemDuration = if (mediaConfig?.isOriginPathPlay == true) {
            currentMediaItem!!.videoOriginDuration
        } else {
            currentMediaItem!!.finalDuration
        }
        if (ConstantMediaSize.themeType == ThemeEnum.NONE) {
            return
        }
        //TODO后续转为kotlin后引用方法索引
        if (BUS!!.isTimeTheme()) {
            return
        }
        if (currentMediaItem?.isVideo == true) {
            return
        }
        //不取消最后一张图的出场
        itemDuration = if ((currentIndex == mMediaItems!!.size - 1))
            itemDuration - ConstantMediaSize.themeType.endOffset
        else
            itemDuration
    }

    override fun start(autoPlay: Boolean) {
        currentProgress = 0
        if (autoPlay) {
            isPlaying = autoPlay
            startRender()
        }
    }

    override fun pause() {
        isPlaying = false
        videoPlayer?.pause()
        //nextVideoPlayer?.pause()
    }

    override fun resume() {
        isPlaying = true
        //        mImagePlayer.playResume();
        handler!!.removeMessages(RENDER_CODE)
        startRender()
        videoPlayer?.resume()
        //nextVideoPlayer?.pause()
    }

    override fun onDestroy() {
        mIMediaCallback = null
        handler!!.removeCallbacksAndMessages(null)
        handlerThread!!.quitSafely()
        videoPlayer?.onDestroy()
        nextVideoPlayer?.onDestroy()
    }

    override fun onVideoPause() {}

    //重置当前
    override fun videoReset() {
        if (currentMediaItem is VideoMediaItem) {
            createVideoPlay(false)
        }
    }

    override fun setDataSource(dataSource: List<MediaItem>?, mediaConfig: MediaConfig?) {
        mMediaItems = dataSource
        this.mediaConfig = mediaConfig;
        currentMediaItem = mMediaItems?.get(0)
    }

    override fun previewMediaItem(index: Int) {}


    /**
     * 片段切换
     */
    fun switchPlayer(curIndex: Int, isPreview: Boolean) {
        LogUtils.v("render", "switchPlayer...");
        this.currentIndex = curIndex;
        currentMediaItem = mMediaItems?.get(curIndex)
        currentProgress = 0
        if (isPreviewTransition) {
            isPreviewTransition = false;
            isTransitioning = true;
        }
        videoPlayer?.pause()
        if (currentMediaItem!!.isImage) {
            videoPlayer?.release()
            if (!isPreview) {
                isPlaying = true
                startRender()
            }
            return
        }
        if (nextVideoPlayer != null&&!isPreview) {
            videoPlayer?.release()
            LogUtils.v("render", "switchPlayer...1");
            transitionFinish(isPreview)
        } else {
            LogUtils.v("render", "switchPlayer...2");
            createVideoPlay(!isPreview)
        }
        if (!isPreview) {
            isPlaying = true
            startRender()
        }
    }


    inner class InnerVideoListener(val autoPlay: Boolean, val tempplay: MediaCodecPlayer?, val call: (() -> Unit)?) : IDecodeStateListenr {
        override fun decoderFinish(iDecode: IDecode?) {
        }

        override fun updateNextFrame() {
            mIMediaCallback?.render()
        }

        override fun readyPlay() {
            if (autoPlay) {
                videoPlayer?.resume()
            } else {
                mIMediaCallback?.render()
            }
            tempplay?.release()
            call?.invoke()
        }

        override fun decoderPrepare(iDecode: IDecode?) {

        }
    }

    inner class InnerAudioListener(val autoPlay: Boolean) : AudioDecodeListener {
        override fun audioReadyPlay() {
            if (autoPlay) {
                videoPlayer?.resumeAudio()
            }
        }
    }


    /**
     * 创建一个videoplayer对象
     */
    fun createVideoPlay(play: Boolean) {
        val tempplay = videoPlayer;
        videoPlayer?.release()

        LogUtils.v("render", "createVideoPlay..");
        if (currentMediaItem is VideoMediaItem) {
            videoPlayer = MediaCodecPlayer(Triple((currentMediaItem as VideoMediaItem).getFinalPath(mediaConfig?.isOriginPathPlay
                ?: false),
                currentMediaItem!!.speed, false), isMurging, InnerVideoListener(play, tempplay, null), InnerAudioListener(play))
            if (videoPlayer?.getOutTexture() == 0) {
                videoPlayer?.onSurfaceCreated()
            }
            currentMediaItem?.videoTexture = videoPlayer!!.getOutTexture()
            videoPlayer?.setVolume((currentMediaItem as VideoMediaItem).playVolume)
            LogUtils.v("render", "createVideoPlay..videoTexture:" + currentMediaItem?.videoTexture + "," + currentMediaItem?.path);
            // 这句很重要，是告诉surface转gl过程中，有帧来了
        }
    }

    /**
     * 主题片段切换时，如果下一个片段是视频片段，则对视频片段进行重赋值
     */
    fun createNextVideoPlay(nextMediaItem: VideoMediaItem) {
        LogUtils.v("render", "createNextVideoPlay...");
        nextVideoPlayer = MediaCodecPlayer(Triple(nextMediaItem.getFinalPath(mediaConfig?.isOriginPathPlay
            ?: false), nextMediaItem.speed, false), isMurging, null, null)
        if (nextVideoPlayer?.getOutTexture() == 0) {
            nextVideoPlayer?.buiSurface()
        }
        nextMediaItem.videoTexture = nextVideoPlayer!!.getOutTexture()
        nextVideoPlayer?.setVolume(nextMediaItem.playVolume)
        nextVideoPlayer?.execute(false)
        LogUtils.v("render", "createNextVideoPlay..videoTexture:" + nextMediaItem.videoTexture + "," + nextMediaItem.path);
    }

    /**
     * 转场播放结束,nextVideoPlayer切换到videoplayer
     */
    fun transitionFinish(isPreview: Boolean) {
        videoPlayer = nextVideoPlayer;
        videoPlayer?.listener = this;
        if (!isPreview) {
            videoPlayer?.concatAudioPlay()
            videoPlayer?.resume()
        }
    }

    /**
     * 是否转场预览
     */
    override fun previewTransition(flag: Boolean) {
        isPreviewTransition = flag
    }

    /**
     * currenIndex的切换放到这里进行控制
     *
     * @param seekPair first:seekIndex second:progress
     * @param renderPair first:forceRender  second:渲染一次   third:requestRender环境
     */
    fun setSeekTo(seekPair: Pair<Int, Int>, renderPair: Triple<Boolean, Boolean, Boolean>, callback: (() -> Unit)?) {
        LogUtils.v(TAG, "setSeekTo-num:" + renderTaskfSeek.size);
        if (renderTaskfSeek.contains(RenderSingleType.SEEK_TO)) {
            return
        }
        renderTaskfSeek.add(RenderSingleType.SEEK_TO)
        LogUtils.v(TAG, "currentIndex:" + currentIndex + ",setSeekTo-seekPair:" + seekPair + ",renderPair:" + renderPair);
        currentProgress = seekPair.second.toLong()
        if (currentIndex != seekPair.first || renderPair.first) {
            currentIndex = seekPair.first
            currentMediaItem = mMediaItems!![currentIndex]
            videoPlayer?.release()
            if (currentIndex != seekPair.first) {
                nextVideoPlayer?.release()
            }
            if (currentMediaItem!!.isVideo) {
                val volume = (currentMediaItem as VideoMediaItem).playVolume;
                videoPlayer = MediaCodecPlayer(Triple((currentMediaItem as VideoMediaItem).getFinalPath(mediaConfig?.isOriginPathPlay
                    ?: false),
                    currentMediaItem!!.speed, false), isMurging,
                    InnerVideoListener(isPlaying, null, {
                        if (currentProgress != 0L) {
                            videoPlayer?.seek(currentProgress, renderTaskfSeek, isPlaying)
                        } else {
                            renderTaskfSeek.clear()
                        }
                        videoPlayer?.setVolume(volume)
                    }), InnerAudioListener(isPlaying))
                if (renderPair.third) {
                    videoSeekRender(callback)
                } else {
                    BUS?.requestRender {
                        videoSeekRender(callback)
                    }
                }
                return
            }
            if (renderPair.third) {
                BUS?.seekToImpl(seekPair, renderPair)
                renderTaskfSeek.clear()
            } else {
                BUS?.requestRender {
                    BUS?.seekToImpl(seekPair, renderPair)
                    renderTaskfSeek.clear()
                    LogUtils.v(TAG, "BUS?.requestRender-renderTaskfSeek..1");
                }
            }
            return
        }

        if (currentMediaItem!!.isVideo) {
            LogUtils.v(TAG, "setSeekTo-index1:$seekPair , $renderPair");
            videoPlayer?.seek(currentProgress, renderTaskfSeek, isPlaying)
            if (renderPair.second) {
                mIMediaCallback?.render()
            }
            callback?.invoke()
            return
        }
        //视频seek
        BUS?.seekTheme(seekPair, renderPair)
        renderTaskfSeek.clear()
        LogUtils.v(TAG, "BUS?.requestRender-renderTaskfSeek..2");
    }


    fun videoSeekRender(callback: (() -> Unit)?) {
        videoPlayer?.createAndExcute()
        currentMediaItem?.videoTexture = videoPlayer!!.getOutTexture()
        videoPlayer?.setVolume((currentMediaItem as VideoMediaItem).playVolume)
        BUS?.clearPreMediaPrepare()
        callback?.invoke()
    }


    override fun setRatio() {
//        mImagePlayer.setRatio();
    }

    override fun setPureColor(bgInfo: BGInfo) {}

    /**
     * 设置缩放
     *
     * @param bgInfo
     */
    override fun setZoomScale(bgInfo: BGInfo) {
        BUS!!.updateBgInfo(bgInfo)
    }

    override fun seekToEnd() {
        videoPlayer?.interruptSeeking()
        clearSeekTask()
    }

    override fun setScaleTranlation(scale: Float, x: Int, y: Int, originX: Int, originY: Int) {

    }

    override fun setVolume(volume: Float) {
        videoPlayer?.setVolume(volume)
    }

    override fun updateTransitionAndAfilterVideo() {}
    override fun setFilterStrength(progress: Float, renderSingleTasks: MutableList<String>) {
        BUS!!.requestRender {
            renderSingleTasks.remove(RenderSingleType.FILTER)
            BUS!!.imagePreparePreView()
        }
    }

    override fun setWidgetDataSource(list: List<Bitmap>?) {
//        mImagePlayer.setWidgetMimaps(list);
    }

    override fun previewAfilter() {
//        mImagePlayer.previewFilter(currentMediaItem);
    }

    override fun doMediaRotate() {
        BUS!!.requestRender { BUS!!.imagePreparePreView() }
    }

    override fun doMediaMirror() {}
    override fun setClipPreview(flag: Boolean) {
        currentProgress = 0
    }

    override fun changeTheme(mediaConfig: MediaConfig) {}
    override fun getOutputTexture(): Int {
        return 0
    }

    override fun getCurPosition(): Int {
        return currentProgress.toInt();
    }

    override fun setCurrentMediaItem(mediaItem: MediaItem?) {
        currentMediaItem = mediaItem
        currentIndex = mMediaItems!!.indexOf(mediaItem)
    }

    override fun setCurrenPostion(currenPostion: Int) {}

    /**
     * 当前视频的texture
     */
    override fun getPreTexture(): Int {
        return videoPlayer?.getOutTexture() ?: 0;
    }

    override fun getNextTexture(): Int {
        return 0
    }

    companion object {
        private const val RENDER_CODE = 100
    }

    override fun decoderFinish(iDecode: IDecode?) {
        mIMediaCallback?.onComplet()
    }

    override fun removeTransition() {
        isPreviewTransition = false;
    }

    override fun updateNextFrame() {
        mIMediaCallback?.render()
    }

    /**
     * 设置pts
     */
    override fun setCurrentMurPts(pts: Long?) {
        videoPlayer?.setCurrentMurPts(pts)
    }

    override fun setvideoSpeed(speed: Float) {
        videoPlayer?.setSpeed(speed)
    }

    fun clearSeekTask() {
        renderTaskfSeek.clear()
    }
}