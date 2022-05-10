package com.zhengsr.zplayer.player

/**
 * @author by zhengshaorui 2022/5/5
 * describe：播放器工厂类
 */
abstract class PlayerFactory<T : AbsPlayer> {
    abstract fun createPlayer(): T
}