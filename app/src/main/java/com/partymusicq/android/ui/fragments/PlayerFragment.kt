package com.partymusicq.android.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.partymusicq.android.R
import com.partymusicq.android.enums.SpotifyEventEnum
import com.partymusicq.android.event.SpotifyEvent
import com.partymusicq.android.ui.adapter.TrackProgress
import com.partymusicq.android.util.UtilPlayer
import com.partymusicq.android.util.UtilSpotify
import com.partymusicq.android.util.UtilSpotify.Companion.spotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.Track

class PlayerFragment : Fragment(), UtilSpotify.SpotifyListener {

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

        UtilSpotify.init(this, context)
        if (spotifyAppRemote == null || !spotifyAppRemote!!.isConnected) {
            UtilSpotify.logIntoSpotify(SpotifyEvent(SpotifyEventEnum.LoginAndStartPlaying))
        } else {
            onConnected()
        }

        return view
    }

    override fun onConnected() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: Track = it.track
            trackTextView.text = track.name
            artistText.text = track.artist.name
            spotifyAppRemote?.imagesApi?.getImage(track.imageUri, Image.Dimension.LARGE)
                ?.setResultCallback { bitmap ->
                    albumArtImageView.setImageBitmap(bitmap)
                }
            trackProgress.setDuration(track.duration)
            trackProgress.update(it.playbackPosition)
            songDurationText.text = UtilPlayer.msToFormattedTime(track.duration)
            if(it.isPaused){
                playPauseButton.setImageResource(R.drawable.btn_play)
                trackProgress.pause()
            } else {
                playPauseButton.setImageResource(R.drawable.btn_pause)
                trackProgress.unPause()
            }
        }
    }

    private fun setupOnClicks() {
        prevButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        playPauseButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (spotifyAppRemote == null || spotifyAppRemote?.isConnected != true) {
                    UtilSpotify.logIntoSpotify(SpotifyEvent(SpotifyEventEnum.LoginAndPlayPause))
                } else {
                    UtilSpotify.playPause()
                }
            }
        })
        nextButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()

        const val TAG = "PlayerFragment"
    }
}