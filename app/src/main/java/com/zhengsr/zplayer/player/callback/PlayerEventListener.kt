package com.zhengsr.zplayer.player.callback

/**
 * @author by zhengshaorui 2022/5/5
 * describe：播放器状态
 */
interface PlayerEventListener {
    fun onError()

    fun onCompletion()

    fun onInfo(what: Int, extra: Int)

    fun onPrepared()

    fun onVideoSizeChanged(width: Int, height: Int)
}