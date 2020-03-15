package com.partymusicq.android.util

import android.content.Context
import android.util.Log
import com.partymusicq.android.enums.SpotifyEventEnum
import com.partymusicq.android.event.SpotifyEvent
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException

class UtilSpotify {

    interface SpotifyListener {
        fun onConnected()
    }

    companion object {

        fun init(spotifyListener: SpotifyListener, context: Context?) {
            this.spotifyListener = spotifyListener
            this.context = context
        }

        fun logIntoSpotify(event: SpotifyEvent) {
            SpotifyAppRemote.disconnect(spotifyAppRemote);
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URL)
                .showAuthView(true)
                .build()
            val connectionListener = object : Connector.ConnectionListener {
                override fun onConnected(spotAppRemote: SpotifyAppRemote) {
                    Log.d(TAG, "Connected to Spotify!")
                    spotifyAppRemote = spotAppRemote
                    spotifyAppRemote?.userApi?.capabilities?.setResultCallback {
                        canPlayOnDemand = it.canPlayOnDemand
                        if (canPlayOnDemand) {
                            spotifyListener.onConnected()
                            when (event.event) {
                                SpotifyEventEnum.LoginAndStartPlaying -> startPlaying()
                                SpotifyEventEnum.LoginAndSeek -> seekTo(event.seekPos)
                                SpotifyEventEnum.LoginAndPlayPause -> playPause()
                                else -> {}
                            }
                        } else {
                            Log.e(TAG, "user can't play on demand")
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e(TAG, throwable.message, throwable)
                    when (throwable) {
                        is SpotifyConnectionTerminatedException -> {
                            logIntoSpotify(SpotifyEvent(SpotifyEventEnum.LoginOnly))
                            Log.d(TAG, "connection terminated, logging back in")
                        }
                        is CouldNotFindSpotifyApp -> Log.e(TAG, "Spotify App not installed!")
                        is NotLoggedInException -> Log.e(TAG, "Not Logged in with Spotify!")
                        else -> Log.e(TAG, "Failed to Connect to Spotify!")
                    }
                }
            }
            if (context != null) {
                SpotifyAppRemote.connect(context, connectionParams, connectionListener)
            } else {
                Log.e(TAG, "context is null, did you init first?")
            }
        }

        fun startPlaying() {
            spotifyAppRemote?.connectApi?.connectSwitchToLocalDevice()
            //TODO: remove this whenever we have the ability to create a song queue
            spotifyAppRemote?.playerApi?.setShuffle(true)
            spotifyAppRemote?.playerApi?.play("spotify:playlist:6WGyoQqHDANYrzJJZFWZqe")
        }

        fun seekTo(pos: Long?) {
            if (pos != null) {
                spotifyAppRemote?.connectApi?.connectSwitchToLocalDevice()
                UtilSpotify.spotifyAppRemote?.playerApi?.seekTo(pos)
                    ?.setErrorCallback{ Log.e(TAG, "Something went wrong, $it")}
            } else {
                Log.e(TAG, "seek position is null")
            }
        }

        fun playPause() {
            spotifyAppRemote?.connectApi?.connectSwitchToLocalDevice()
            spotifyAppRemote?.playerApi?.playerState?.setResultCallback { playerState ->
                if(playerState.isPaused){
                    resume()
                } else {
                    pause()
                }
            }
        }

        private fun resume() {
            spotifyAppRemote?.playerApi
                ?.resume()
                ?.setResultCallback { Log.d(TAG, "resumed") }
                ?.setErrorCallback { Log.e(TAG, "Error resuming") }
        }

        private fun pause() {
            spotifyAppRemote?.playerApi
                ?.pause()
                ?.setResultCallback { Log.d(TAG, "paused") }
                ?.setErrorCallback { Log.d(TAG, "Error pausing") }
        }

        @JvmStatic
        var spotifyAppRemote : SpotifyAppRemote? = null

        @JvmStatic
        lateinit var spotifyListener: SpotifyListener

        @JvmStatic
        var context: Context? = null

        @JvmStatic
        var canPlayOnDemand = false

        private const val TAG = "UtilSpotify"
        private const val REDIRECT_URL = "http://com.partymusicq.android/callback"
        private const val CLIENT_ID = "790890a4a7ab4ec39238e088d4ef2202"
    }
}