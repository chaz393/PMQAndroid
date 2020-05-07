package com.partymusicq.android.pojo

class SongQueue(private var songQueue: ArrayList<Song>) {

    init {
        sortQueue()
    }

    fun popQueue() {
        if (songQueue.isNotEmpty()) {
            songQueue.removeAt(0)
            for (song in songQueue) {
                song.position -= 1
            }
            sortQueue()
            //todo update db
        }
    }

    fun getFirstSongUri(): String? {
        if (songQueue.isNotEmpty()) {
            return songQueue[0].uri
        } else {
            return null
        }
    }

    fun queueIsNotEmpty(): Boolean {
        return songQueue.isNotEmpty()
    }

    fun addSongToQueue(uri: String) {
        val position = songQueue.size
        val newSong = Song(uri, position)
        songQueue.add(newSong)
        //todo add song to db and get doc id to store on song
    }

    private fun sortQueue() {
        songQueue = ArrayList(songQueue.sortedWith(compareBy {it.position}))
    }
}