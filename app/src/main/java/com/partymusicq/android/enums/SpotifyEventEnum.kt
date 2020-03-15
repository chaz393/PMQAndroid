package com.partymusicq.android.enums

enum class SpotifyEventEnum(val id: Int) {
    LoginOnly(0),
    LoginAndSeek(1),
    LoginAndPlayPause(2),
    LoginAndStartPlaying(3)
}