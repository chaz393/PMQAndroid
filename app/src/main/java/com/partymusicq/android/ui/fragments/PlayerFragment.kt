package com.partymusicq.android.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.partymusicq.android.R
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.Track

class PlayerFragment : Fragment() {

    private lateinit var albumArtImageView: AppCompatImageView
    private lateinit var trackTextView: TextView
    private lateinit var seekBar: AppCompatSeekBar
    private lateinit var trackProgress: TrackProgress
    private lateinit var songDurationText: TextView
    private lateinit var songPositionText: TextView
    private lateinit var artistText: TextView
    private lateinit var prevButton: AppCompatImageButton
    private lateinit var playPauseButton: AppCompatImageButton
    private lateinit var nextButton: AppCompatImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_fragment, container, false)

        albumArtImageView = view.findViewById(R.id.album_art)
        trackTextView = view.findViewById(R.id.track_title)
        seekBar = view.findViewById(R.id.seek_bar)
        songDurationText = view.findViewById(R.id.song_duration_text)
        songPositionText = view.findViewById(R.id.song_position_text)
        artistText = view.findViewById(R.id.artist)
        prevButton = view.findViewById(R.id.prev_button)
        playPauseButton = view.findViewById(R.id.play_pause_button)
        nextButton = view.findViewById(R.id.next_button)

        trackProgress = TrackProgress(seekBar, songPositionText)

        setupOnClicks()

        if (spotifyAppRemote == null) logIntoSpotify() else onConnected()
        return view
    }

    private fun setupOnClicks() {
        prevButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        playPauseButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                spotifyAppRemote?.playerApi?.playerState?.setResultCallback { playerState ->
                    if(playerState.isPaused){
                        spotifyAppRemote!!.playerApi
                            .resume()
                            .setResultCallback { Log.d(TAG, "resumed") }
                            .setErrorCallback { Log.e(TAG, "Error resuming") }
                    } else {
                        spotifyAppRemote!!.playerApi
                            .pause()
                            .setResultCallback { Log.d(TAG, "paused") }
                            .setErrorCallback { Log.e(TAG, "Error pausing") }
                    }
                }
            }
        })
        nextButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun logIntoSpotify() {
        SpotifyAppRemote.disconnect(spotifyAppRemote);
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URL)
            .showAuthView(true)
            .build()
        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotAppRemote: SpotifyAppRemote) {
                spotifyAppRemote = spotAppRemote
                Log.d("PlayerFragment", "Connected to Spotify!")
                //TODO: remove this whenever we have the ability to create a song queue
                spotifyAppRemote?.playerApi?.play("spotify:track:5hkyCYlfxi91HcfMDrlCKt")
                onConnected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("PlayerFragment", throwable.message, throwable)
                when (throwable) {
                    is CouldNotFindSpotifyApp -> Log.e("PlayerFragment", "Spotify App not installed!")
                    is NotLoggedInException -> Log.e("PlayerFragment", "Not Logged in with Spotify!")
                    else -> Log.e("PlayerFragment", "Failed to Connect to Spotify!")
                }
            }
        }
        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    private fun onConnected() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: Track = it.track
            trackTextView.text = track.name
            artistText.text = track.artist.name
            spotifyAppRemote!!.imagesApi.getImage(track.imageUri, Image.Dimension.LARGE)
                .setResultCallback { bitmap ->
                    albumArtImageView.setImageBitmap(bitmap)
                }
            if(it.isPaused){
                playPauseButton.setImageResource(R.drawable.btn_play)
                trackProgress.pause()
            } else {
                playPauseButton.setImageResource(R.drawable.btn_pause)
                trackProgress.unPause()
            }
            trackProgress.setDuration(track.duration)
            trackProgress.update(it.playbackPosition)
            songDurationText.text = msToFormattedTime(track.duration)
        }
    }

    //TODO: it would be nice if this had it's own file
    class TrackProgress(val seekBar: SeekBar,
                        val progressText: TextView,
                        val handler: Handler = Handler()){

        companion object {
            const val LOOP_DURATION : Long = 500
        }

        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                spotifyAppRemote?.playerApi?.seekTo(seekBar.progress.toLong())
                    ?.setErrorCallback{
                        Log.e(TAG, "Something is Wrong")
                    }
            }
        }

        init {
            seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        }

        private val seekRunnable : Runnable = object : Runnable {
            override fun run() {
                val progress = seekBar.progress
                seekBar.progress = (progress + LOOP_DURATION).toInt()
                progressText.text = msToFormattedTime(progress + LOOP_DURATION)
                handler.postDelayed(this, LOOP_DURATION)
            }
        }

        fun setDuration(duration: Long){
            seekBar.max = duration.toInt()
        }

        fun update(progress : Long){
            seekBar.progress = progress.toInt()
            progressText.text = msToFormattedTime(progress)
        }

        fun pause(){
            handler.removeCallbacks(seekRunnable)
        }

        fun unPause(){
            handler.removeCallbacks(seekRunnable)
            handler.postDelayed(seekRunnable, LOOP_DURATION)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()

        const val TAG = "PlayerFragment"
        const val REDIRECT_URL = "http://com.partymusicq.android/callback"
        const val CLIENT_ID = "790890a4a7ab4ec39238e088d4ef2202"

        @JvmStatic
        var spotifyAppRemote : SpotifyAppRemote? = null

        //TODO: probably move this over to a util
        fun msToFormattedTime(ms: Long): String {
            val sec = (ms/1000)%60
            val min = (ms/1000/60)%60
            return String.format("%02d:%02d", min, sec)
        }
    }
}