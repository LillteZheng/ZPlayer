package com.zhengsr.zplayer.player

import android.view.Surface
import com.zhengsr.zplayer.player.callback.PlayerEventListener
import com.zhengsr.zplayer.player.status.PlayerStatus

/**
 * @author by zhengshaorui 2022/5/5
 * describe：播放器的基类，用于初始化，播放等操作
 */
abstract class AbsPlayer {
    protected var curStatus = PlayerStatus.STATE_IDLE
    protected var listener: PlayerEventListener? = null
    abstract fun initPlayer()

    abstract fun start()
    abstract fun release()
    abstract fun isPlaying():Boolean
    abstract fun pause()
    abstract fun setSurface(surface: Surface)

    /**
     * 获取当前播放的位置
     */
    abstract fun getCurrentPosition(): Int

    /**
     * 获取视频总时长
     */
    abstract fun getDuration(): Int

    /**
     * 获取缓冲百分比
     */
    abstract fun getBufferedPercentage(): Int

    fun setPlayerEventListener(listener: PlayerEventListener) {
        this.listener = listener
    }

}