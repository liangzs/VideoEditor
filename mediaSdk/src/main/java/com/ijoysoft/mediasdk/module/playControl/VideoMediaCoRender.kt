package com.ijoysoft.mediasdk.module.playControl

import android.graphics.SurfaceTexture
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.common.utils.ObjectUtils
import com.ijoysoft.mediasdk.module.entity.*
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecode
import com.ijoysoft.mediasdk.module.mediacodec.decode.IDecodeStateListenr
import com.ijoysoft.mediasdk.module.mediacodec.decode.MediaCodecPlayer
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus
import com.ijoysoft.mediasdk.module.playControl.VideoRichRender.TransitionCallback
import com.ijoysoft.mediasdk.view.RenderSingleType

/**
 * 采用mediacodec进行视频的拉取刷新,提供纹理对象，不再做渲染
 */
class VideoMediaCoRender : IMediaRender, IDecodeStateListenr {
    private var mIMediaCallback: IMediaCallback? = null
    private var currentMediaItem: MediaItem? = null

    //渲染总线
    private var BUS: MediaRenderBus? = null
    private var currentPosition = 0
    private var currentVideoIndex = 0
    private var medialist: List<MediaItem>? = null
    private var videoLists: MutableList<MediaItem?>? = null

    //视频的补充渲染
    private var mVideoPlayer: VideoRichRender? = null

    //当前主渲染视频,只维持一个渲染推动(目前需要手动进行requestRender渲染)：setOnFrameAvailableListener
    private var currentSurfaceTexture: SurfaceTexture? = null

    //当前主渲染视频,通过videoList切换解码视频
    private var curentCodecPlayer: MediaCodecPlayer? = null

    //下一渲染视频,当两者同时出现的时候，音频播放音乐取一播放
    private val nextCodecPlayer: MediaCodecPlayer? = null

    //原始片段播放
    private var transitionCallback: TransitionCallback? = TransitionCallback { pause() }
    override fun onSurfaceCreated() {
        mVideoPlayer?.onSurfaceCreated()
        curentCodecPlayer?.onSurfaceCreated()
        currentSurfaceTexture = curentCodecPlayer?.getSurfaceTexture()
        // 这句很重要，是告诉surface转gl过程中，有帧来了
        currentSurfaceTexture?.setOnFrameAvailableListener { surfaceTexture: SurfaceTexture? -> mIMediaCallback?.render() }
    }

    override fun onSurfaceChanged(offsetX: Int, offsetY: Int, width: Int, height: Int, screenWidth: Int, screenHeight: Int) {
        LogUtils.i(TAG, "onSurfaceChanged...")
        mVideoPlayer?.onSurfaceChanged(0, 0, width, height, screenWidth, screenHeight)
    }

    override fun onDrawFrame() {
        mVideoPlayer?.setCurrentVideoDuration(currentPosition)
        mVideoPlayer?.onDrawFrame()
    }

    override fun onDrawFrameStatus(): ActionStatus {
        mVideoPlayer?.setCurrentVideoDuration(currentPosition)
        curentCodecPlayer?.onDrawFrame()
        return mVideoPlayer?.onDrawFrameStatus() ?: ActionStatus.STAY
    }

    override fun init(bus: MediaRenderBus, callback: IMediaCallback) {
        mVideoPlayer = VideoRichRender()
        mVideoPlayer?.setTransitionCallback(transitionCallback)
        mIMediaCallback = callback
        BUS = bus
    }

    override fun start(autoPlayer: Boolean) {
        //TODO("播放设置")
        updateTransitionAndAfilterVideo()
        if (autoPlayer) {
            updateVideoPlayeInfo()
            curentCodecPlayer?.resume()
        }
    }

    override fun pause() {
        curentCodecPlayer?.pause()
    }

    override fun resume() {
        curentCodecPlayer?.resume()
        updateVideoPlayeInfo()
        if (mIMediaCallback != null) {
            mIMediaCallback?.render()
        }
    }

    /**
     * 更新音量，变速等视频信息
     * TODO(设置音量)
     */
    private fun updateVideoPlayeInfo() {
        if (currentMediaItem == null) {
            return
        }
        //        float volume = ((VideoMediaItem) currentMediaItem).getPlayVolume();
//        ExoPlayerSingle.INSTANCE.setVolume(volume);
//        if (originPlay) {
//            ExoPlayerSingle.INSTANCE.setSpeed(1);
//            return;
//        }
//        float speed = currentMediaItem.getVideoSpeed();
//        speed = speed == 0 ? 1 : speed;
//        ExoPlayerSingle.INSTANCE.setSpeed(speed);
    }

    /**
     * 因为视频的播放作为一个单例，所以最后清理时，记得一定要把数据清理，解绑
     */
    override fun onDestroy() {
        mIMediaCallback = null
        transitionCallback = null
        curentCodecPlayer?.release()
        mVideoPlayer?.onDestroy()
    }

    override fun setDataSource(dataSource: List<MediaItem>,mediaConfig: MediaConfig) {
        setDataSource(dataSource, false)
    }

    override fun onMainDestroy() {
//        curentCodecPlayer.release();
    }

    override fun getPreTexture(): Int {
        return 0
    }

    override fun getNextTexture(): Int {
        return 0
    }

    override fun onVideoPause() {
//        curentCodecPlayer.stop();
    }

    override fun videoReset() {
//        surfaceTexture = mVideoPlayer.getSurfaceTexture();
        // 这句很重要，是告诉surface转gl过程中，有帧来了
//        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
//            @Override
//            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//                if (mIMediaCallback != null) {
//                    mIMediaCallback.render();
//                }
//            }
//        });
    }

    override fun setDataSource(dataSource: List<MediaItem>, originPathPlay: Boolean, noSpeedPlay: Boolean) {
        setDataSource(dataSource, originPathPlay)
    }

    fun setDataSource(dataSource: List<MediaItem>, originPath: Boolean) {
        filterVideoItem(dataSource)
        createContatMediaPlayer()
        if (!videoLists!!.isEmpty()) {
            mVideoPlayer?.setVideoMediaItem(videoLists!![0])
        }
        medialist = dataSource
        if (currentVideoIndex > medialist!!.size - 1) {
            currentVideoIndex = medialist!!.size - 1
        }
    }

    /**
     * 过滤出视频频片段
     */
    private fun filterVideoItem(srcList: List<MediaItem>) {
        videoLists = ArrayList()
        if (ObjectUtils.isEmpty(srcList)) {
            return
        }
        for (mediaItem in srcList) {
            if (mediaItem == null) {
                continue
            }
            if (mediaItem.mediaType == MediaType.VIDEO) {
                videoLists?.add(mediaItem as VideoMediaItem)
            }
        }
    }

    /**
     * 通过currentVideoIndex进行seekto预览
     *
     * @param index
     */
    override fun previewMediaItem(index: Int) {
        currentVideoIndex = getVideoIndex(index)
        updateTransitionAndAfilterVideo()
        //        mVideoPlayer.setTransitionShow(false);
        currentPosition = 0
        //        ExoPlayerSingle.INSTANCE.seekTo(currentVideoIndex, 0);
        curentCodecPlayer?.seek(currentPosition.toLong(), null, false)
    }

    override fun previewTransition(flag: Boolean) {
        mVideoPlayer?.isPreviewTransition(flag)
    }

    override fun previewTransitionPreViedeo(flag: Boolean) {
        mVideoPlayer?.isPreviewTransition(flag)
        currentVideoIndex = videoIndex
        //seekTo(currentVideoIndex, 0)
        mVideoPlayer?.onVideoChanged(currentMediaItem)
        mVideoPlayer?.setVideoFrameRatioUpdate(currentMediaItem)
        mVideoPlayer?.playPrepare()
        mVideoPlayer?.setCurrentVideoDuration(0)
        currentPosition = 0
        mIMediaCallback?.render()
        //        ExoPlayerSingle.INSTANCE.start();
        updateVideoPlayeInfo()
    }

    override fun switchPlayer(curIndex: Int) {
        LogUtils.v("VideoMediaRender", "params:curIndex:$curIndex")
        currentPosition = 0
        updateTransitionAndAfilterVideo()
        updateVideoPlayeInfo()
        //重置为0重头播放
        //seekTo(getVideoIndex(curIndex), 0)
        mIMediaCallback?.render()
    }

    @Volatile
    private var seeking = false

    /**
     * 从新计算index在videolist中的索引
     *
     * @param i
     * @param ti
     * @param forceRender
     */
    override fun setSeekTo(i: Int, ti: Int, forceRender: Boolean) {
        seekImple(i, ti, forceRender)
    }


    override fun setZoomScale(bgInfo: BGInfo) {
        //记录scaleZoom的值
        mVideoPlayer?.setPureColor(bgInfo)
    }

    //    @Override
    //    public void drawNoTransition() {
    //        mVideoPlayer.setTransitionShow(false);
    //    }
    override fun finishReset() {}
    private fun seekImple(i: Int, ti: Int, forceRender: Boolean) {
        if (seeking) {
            return
        }
        val videoIndex = getVideoIndex(i)
        if (i >= medialist!!.size || videoLists!!.isEmpty() || videoIndex == -1 || currentVideoIndex >= videoLists!!.size) {
            return
        }
        LogUtils.i(TAG, "i:$i,ti:$ti,videoIndex:$videoIndex,currentVideoIndex:$currentVideoIndex")
        if (currentVideoIndex != videoIndex || forceRender) {
            currentMediaItem = videoLists!![videoIndex]
            seeking = true
            updateTransitionAndAfilterVideo()
        }
        currentVideoIndex = videoIndex
        //如果视频有进行加速和减速情况的，要进行还原
        currentPosition = ti
        if (currentMediaItem != null && currentMediaItem?.speed != 0f) {
            currentPosition *= currentMediaItem!!.speed.toInt()
        }
        curentCodecPlayer?.seek(currentPosition.toLong(), null,false)
    }

    override fun seekToEnd() {}

    /**
     * 更改显示区域，滤镜，转场，都在这里完成
     */
    override fun updateTransitionAndAfilterVideo() {
        if (currentMediaItem == null) {
            return
        }
        BUS?.requestRender {
            mVideoPlayer?.onVideoChanged(currentMediaItem)
            mVideoPlayer?.setVideoFrameRatioUpdate(currentMediaItem)
            mVideoPlayer?.playPrepare()
            if (currentMediaItem?.afilter != null
                && currentMediaItem!!.afilter.getmFilterType() != MagicFilterType.NONE
            ) {
                mVideoPlayer?.changeFilter(currentMediaItem?.afilter)
            } else {
                mVideoPlayer?.changeFilter(GPUImageFilter())
            }
            seeking = false
        }
    }

    override fun checkSupportTransition(): Boolean {
        return true
    }

    override fun switchNotVideo() {
        currentPosition = 0
    }

    override fun setFilterStrength(progress: Float, renderSingleTasks: MutableList<String>) {
        BUS?.requestRender {
            mVideoPlayer?.setFilterStrength(progress)
            mIMediaCallback?.render()
            renderSingleTasks.remove(RenderSingleType.FILTER)
        }
    }

    override fun setRatio() {}
    override fun setPureColor(bgInfo: BGInfo) {
        BUS?.requestRender { mVideoPlayer?.setPureColor(bgInfo) }
    }

    override fun setScaleTranlation(scale: Float, x: Int, y: Int, originX: Int, originY: Int) {
        // 需要重置matrix
        if (currentMediaItem?.mediaMatrix == null) {
            currentMediaItem?.mediaMatrix = MediaMatrix(scale, x, y, originX, originY)
        } else {
            currentMediaItem!!.mediaMatrix.scale = scale
            currentMediaItem!!.mediaMatrix.offsetX = x
            currentMediaItem!!.mediaMatrix.offsetY = y
            currentMediaItem!!.mediaMatrix.originX = originX
            currentMediaItem!!.mediaMatrix.originY = originY
        }
        updateTransitionAndAfilterVideo()
        if (mIMediaCallback != null) {
            mIMediaCallback?.render()
        }
    }

    override fun previewAfilter() {
        mVideoPlayer?.changeFilter(currentMediaItem?.afilter)
    }

    override fun doMediaRotate() {
        updateTransitionAndAfilterVideo()
    }

    override fun doMediaMirror() {
        updateTransitionAndAfilterVideo()
    }

    override fun getOutputTexture(): Int {
        return mVideoPlayer?.outputTexture ?: 0
    }

    override fun getCurPosition(): Int {
        currentPosition = curentCodecPlayer?.getCurrentPosition()?.toInt() ?: 0
        return currentPosition
    }

    override fun setCurrentMediaItem(mediaItem: MediaItem) {
        currentMediaItem = mediaItem
    }

    override fun setCurrenPostion(currenPostion: Int) {
        currentPosition = currenPostion
    }

    override fun setClipPreview(flag: Boolean) {}
    override fun removeTransition() {
        mVideoPlayer?.isPreviewTransition(false)
    }

    override fun setVolume(volume: Float) {
        LogUtils.v("VideoMediaRender", "volume:$volume")
        ExoPlayerSingle.setVolume(volume)
    }

    /**
     * 尝试使用exoplayer的concat连续播放功能
     */
    fun createContatMediaPlayer() {
        if (ObjectUtils.isEmpty(videoLists)) {
            return
        }
        curentCodecPlayer = MediaCodecPlayer(Triple((videoLists!![0] as VideoMediaItem).finalPath, 1f, false),
            false, this, null)
    }

    /**
     * 转化器
     *
     * @return
     */
    fun getMediaIndex(videoIndex: Int): Int {
        return if (ObjectUtils.isEmpty(videoLists) || ObjectUtils.isEmpty(medialist)) {
            0
        } else medialist!!.indexOf(videoLists!![videoIndex])
    }

    fun getVideoIndex(mediaIndex: Int): Int {
        return if (ObjectUtils.isEmpty(videoLists) || ObjectUtils.isEmpty(medialist) || mediaIndex >= medialist!!.size) {
            -1
        } else videoLists!!.indexOf(medialist!![mediaIndex])
    }

    val videoIndex: Int
        get() = if (currentMediaItem == null) {
            0
        } else videoLists!!.indexOf(currentMediaItem)

    override fun setvideoSpeed(speed: Float) {
        //setSpeed(speed)
    }

    override fun setReverse(reverse: Boolean) {}

    /**
     * 重置当前进度
     */
    fun resetCurrentClipProgress() {
        currentPosition = 0
        //seekTo(currentVideoIndex, 0)
    }

    fun onCompletion() {
        currentPosition = 0
        mIMediaCallback?.onComplet(getMediaIndex(currentVideoIndex))
    }

    override fun decoderFinish(iDecode: IDecode) {
        mIMediaCallback?.onComplet()
    }

    override fun updateNextFrame() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "VideoMediaRender"
    }
}