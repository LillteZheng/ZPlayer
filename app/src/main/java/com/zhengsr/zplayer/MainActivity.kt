package com.zhengsr.zplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.zhengsr.zplayer.player.AndroidMediaPlayer
import com.zhengsr.zplayer.player.PlayerFactory
import com.zhengsr.zplayer.player.callback.PlayerEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private  val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
    }

    override fun onStop() {
        super.onStop()
        playerView.onRelease()
    }

    fun play(view: View) {
        playerView.start()
    }

}