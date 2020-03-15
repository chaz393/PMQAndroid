package com.partymusicq.android.event

import com.partymusicq.android.enums.SpotifyEventEnum

class SpotifyEvent {

    var event: SpotifyEventEnum
    var seekPos: Long? = null

    constructor(event: SpotifyEventEnum) {
        this.event = event
    }
    constructor(event: SpotifyEventEnum, seekPos: Long) {
        this.event = event
        this.seekPos = seekPos
    }

}