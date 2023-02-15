package com.ijoysoft.mediasdk.module.playControl

import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.Surface
import androidx.annotation.FloatRange
import com.google.android.exoplayer2.*
import com.ijoysoft.mediasdk.common.global.MediaSdk
import com.ijoysoft.mediasdk.common.utils.LogUtils
import com.ijoysoft.mediasdk.module.entity.VideoMediaItem

/**
 * 视频素材的播放器，用单例，整个视频编辑周期采用同一个视频播放器，减开销
 * 未用身先死，调整为更加精细化的mediacodec自己编写的视频播放
 */
object ExoPlayerSingle : Player.Listener {
    var playCallback: PlayCallback? = null;

    var handler: Handler? = null;
    var handlerThread: HandlerThread? = null;

    /**
     * 单例播放器
     */
    var mExoPlayer: ExoPlayer? = null;

    /**
     * 执行在任务looper内
     */
    fun execute(task: () -> Unit) {
        handler?.post { task.invoke() }
    }

    init {
        initPlay();
    }

    /**
     * exoplayer进行release之后，需要重新创建才行
     */
    fun initPlay() {
        if (mExoPlayer == null) {
            handlerThread = HandlerThread("ExoPlayerSingle");
            handlerThread?.start()
            handler = Handler(Looper.getMainLooper())
            mExoPlayer = ExoPlayer
                .Builder(MediaSdk.getInstance().context)
                .setLooper(handler!!.looper)
                .build().also {
                    val exo = it;
                    exo.addListener(this)
                    execute {
                        exo.repeatMode = Player.REPEAT_MODE_OFF
                    }
                }
        }
    }


    override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
        LogUtils.v("ExoPlayerSingle", "onPositionDiscontinuity reason:" + reason);
        if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
            playCallback?.onCompletion()
        }
    }

    /**
     * 文件片段播放结束之后会回调
     */
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        LogUtils.v("ExoPlayerSingle", "onMediaItemTransition---");
        if (reason == Player.EVENT_MEDIA_ITEM_TRANSITION) {
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        LogUtils.v("ExoPlayerSingle", "playCallback?.onCompletion()");

    }

    /**
     * 设置播放路径
     */
    fun setDataSource(list: List<VideoMediaItem>, originPlay: Boolean) {
        execute {
            //val mediaItems = mutableListOf<MediaItem>().apply {
            //    list.forEach {
            //        LogUtils.v(javaClass.name, "path:" + it.getFinalPath(originPlay));
            //        add(MediaItem.fromUri(Uri.parse(it.getFinalPath(originPlay))))
            //    }
            //}
            //
            //mExoPlayer?.setMediaItems(mediaItems)
            //mExoPlayer?.prepare()
            //mExoPlayer?.play()


            list.forEach {
                mExoPlayer!!.addMediaItem(MediaItem.fromUri(it.getFinalPath(originPlay)))
            }
            mExoPlayer!!.prepare()
        }
    }

    fun seekTo(positionMs: Long) {
        execute {
            mExoPlayer?.seekTo(positionMs)
        }
    }


    fun seekTo(mediaItemIndex: Int, positionMs: Long) {
        execute { mExoPlayer?.seekTo(mediaItemIndex, positionMs) }
    }

    fun stop() {
        execute { mExoPlayer?.stop() }
    }

    /**
     * 清理所有的开销数据
     */
    fun release() {
        execute {
            mExoPlayer?.release()
            mExoPlayer = null;
            handlerThread?.quit()
            handlerThread = null;
        }

    }

    fun setSurface(surface: Surface) = execute { mExoPlayer?.setVideoSurface(surface) }

    fun start() = execute { mExoPlayer?.play() }

    fun pause() = execute { mExoPlayer?.pause() }

    fun setVolume(@FloatRange(from = 0.0, to = 1.0) volume: Float) {
        execute {
            mExoPlayer?.volume = volume
        }
    }

    fun setSpeed(speed: Float) {
        execute { mExoPlayer?.setPlaybackSpeed(speed) }
    }

    /**
     * 退出编辑页面时，需要清理监听
     */
    fun removeListener() {
        mExoPlayer?.removeListener(this)
    }

    fun isPlaying(): Boolean = mExoPlayer!!.isPlaying


    fun setLooper(boolean: Boolean) {
        mExoPlayer?.repeatMode = Player.REPEAT_MODE_OFF
    }


    /**
     * 播放回调
     */
    interface PlayCallback {
        fun onCompletion()
    }


    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)
    }

    /**
     * 所有文件播放结束的时候会回调
     */
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_ENDED) {
            LogUtils.v("ExoPlayerSingle", "playCallback?.onCompletion()");
            playCallback?.onCompletion()
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
    }

}

