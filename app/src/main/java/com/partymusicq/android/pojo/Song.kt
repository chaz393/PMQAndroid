package com.partymusicq.android.pojo

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Song(val uri: String = "",
                var position: Int = 9999,
                var docId: String = "") {

    constructor(song: Song, docId: String) : this(song.uri, song.position, docId)

    companion object {
        const val FIELD_POSITION = "position"
        const val FIELD_URI = "uri"
    }
}