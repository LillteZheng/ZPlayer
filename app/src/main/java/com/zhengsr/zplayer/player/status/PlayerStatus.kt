package com.zhengsr.zplayer.player.status

/**
 * @author by zhengshaorui 2022/5/5
 * describe：播放器的状态
 */
enum class PlayerStatus {
    STATE_IDLE,
    STATE_PREPARING,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED,
    STATE_ERROR,
    STATE_PLAYBACK_COMPLETED
}