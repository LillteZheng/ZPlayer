package com.zhengsr.zplayer.player

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.VideoView
import com.zhengsr.zplayer.player.status.PlayerStatus


/**
 * @author by zhengshaorui 2022/5/5
 * describe：Android 原生 MediaPlayer
 */
class AndroidMediaPlayer : AbsPlayer() {
    companion object {
        private const val TAG = "AndroidMediaPlayer"
    }

    private var mediaPlayer: MediaPlayer? = null
    private var bufferPercent = 0
    override fun initPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener(preparedListener)
            setOnVideoSizeChangedListener(sizeChangedListener)
            setOnCompletionListener(completionListener)
            setOnErrorListener(errorListener)
            setOnInfoListener(infoListener)
            setOnBufferingUpdateListener(bufferingUpdateListener)
        }
    }

    override fun start() {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "start error: $e")
        }
    }

    override fun release() {
        Log.d(TAG, "release() ")
        mediaPlayer?.let {
            it.reset()
            it.release()
            it.setOnPreparedListener(null)
            it.setOnVideoSizeChangedListener(null)
            it.setOnCompletionListener(null)
            it.setOnErrorListener(null)
            it.setOnInfoListener(null)
            it.setOnBufferingUpdateListener(null)
            mediaPlayer = null
            curStatus = PlayerStatus.STATE_IDLE

        }
    }

    override fun isPlaying() = mediaPlayer?.isPlaying ?: false

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun setSurface(surface: Surface) {
        mediaPlayer?.setSurface(surface)
    }

    override fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0

    override fun getDuration() = mediaPlayer?.duration ?:0

    override fun getBufferedPercentage() = bufferPercent

    fun setSurfaceHolder(holder: SurfaceHolder) {
        mediaPlayer?.setDisplay(holder)
    }

    fun setAssert(context: Context, name: String) {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd(name)
        mediaPlayer?.setDataSource(
            fileDescriptor.fileDescriptor,
            fileDescriptor.startOffset,
            fileDescriptor.length
        )
        mediaPlayer?.prepareAsync()
    }

    private val preparedListener = OnPreparedListener { mp ->
        Log.d(TAG, "onPrepared() called with: mp = $mp")
        curStatus = PlayerStatus.STATE_PREPARED
        listener?.onPrepared()
    }
    private var sizeChangedListener =
        OnVideoSizeChangedListener { mp, width, height ->
            Log.d(
                TAG,
                "onVideoSizeChanged() called with: mp = $mp, width = $width, height = $height"
            )
            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight
            if (videoWidth != 0 && videoHeight != 0) {
                listener?.onVideoSizeChanged(width, height)
            }
        }

    private val completionListener = OnCompletionListener {
        Log.d(TAG, "onCompletion() ")
        curStatus = PlayerStatus.STATE_PLAYBACK_COMPLETED
        listener?.onCompletion()
    }

    private val infoListener =
        MediaPlayer.OnInfoListener { mp, what, extra ->
            Log.d(TAG, "onInfo() called with: mp = $mp, what = $what, extra = $extra")
            /**
             * Called to indicate an info or a warning.
            Params:
            mp – the MediaPlayer the info pertains to.
            what – the type of info or warning.
            MEDIA_INFO_UNKNOWN
            MEDIA_INFO_VIDEO_TRACK_LAGGING
            MEDIA_INFO_VIDEO_RENDERING_START
            MEDIA_INFO_BUFFERING_START
            MEDIA_INFO_BUFFERING_END
            MEDIA_INFO_NETWORK_BANDWIDTH (703) - bandwidth information is available (as extra kbps)
            MEDIA_INFO_BAD_INTERLEAVING
            MEDIA_INFO_NOT_SEEKABLE
            MEDIA_INFO_METADATA_UPDATE
            MEDIA_INFO_UNSUPPORTED_SUBTITLE
            MEDIA_INFO_SUBTITLE_TIMED_OUT
            extra – an extra code, specific to the info. Typically implementation dependent.
            Returns:
            True if the method handled the info, false if it didn't. Returning false, or not having
            an OnInfoListener at all, will cause the info to be discarded.
             */
            listener?.onInfo(what, extra)

            true
        }

    private val errorListener =
        OnErrorListener { mp, framework_err, impl_err ->
            Log.d(
                TAG,
                "onError() : mp = $mp, framework_err = $framework_err, impl_err = $impl_err"
            )
            curStatus = PlayerStatus.STATE_ERROR
            listener?.onError()
            true
        }


    private val bufferingUpdateListener =
        OnBufferingUpdateListener { mp, percent ->
            Log.d(TAG, "onBufferingUpdate() : mp = $mp, percent = $percent")
            bufferPercent = percent
        }

}