package com.partymusicq.android.ui.adapter

import android.os.Handler
import android.widget.SeekBar
import android.widget.TextView
import com.partymusicq.android.enums.SpotifyEventEnum
import com.partymusicq.android.event.SpotifyEvent
import com.partymusicq.android.util.UtilPlayer
import com.partymusicq.android.util.UtilSpotify

class TrackProgress(val seekBar: SeekBar,
                    val progressText: TextView,
                    val handler: Handler = Handler()){

    companion object {
        const val LOOP_DURATION : Long = 250
        const val RESYNC_DURATION: Long = 10000
        var lastSyncTime: Long = 0
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            val progress = seekBar.progress.toLong()
            if (UtilSpotify.spotifyAppRemote?.isConnected != true) {
                UtilSpotify.logIntoSpotify(SpotifyEvent(SpotifyEventEnum.LoginAndSeek, progress))
            } else {
                UtilSpotify.seekTo(progress)
            }
        }
    }

    init {
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private val seekRunnable : Runnable = object : Runnable {
        override fun run() {
            var progress = seekBar.progress
            if (progress - lastSyncTime > RESYNC_DURATION) {
                UtilSpotify.spotifyAppRemote?.playerApi?.playerState?.setResultCallback {
                    progress = it.playbackPosition.toInt()
                    lastSyncTime = progress.toLong()
                    seekBar.progress = progress
                    progressText.text = UtilPlayer.msToFormattedTime(progress.toLong())
                }
            } else {
                seekBar.progress = (progress + LOOP_DURATION).toInt()
                progressText.text = UtilPlayer.msToFormattedTime(progress + LOOP_DURATION)
            }
            handler.postDelayed(this, LOOP_DURATION)
        }
    }

    fun setDuration(duration: Long){
        seekBar.max = duration.toInt()
    }

    fun update(progress : Long){
        seekBar.progress = progress.toInt()
        progressText.text = UtilPlayer.msToFormattedTime(progress)
        lastSyncTime = progress
    }

    fun pause(){
        handler.removeCallbacks(seekRunnable)
    }

    fun unPause(){
        handler.removeCallbacks(seekRunnable)
        handler.postDelayed(seekRunnable, LOOP_DURATION)
    }
}