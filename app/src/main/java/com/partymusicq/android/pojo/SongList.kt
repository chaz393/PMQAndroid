package com.partymusicq.android.pojo

class SongList(private var songList: ArrayList<Song>) {

    init {
        sortList()
    }

    fun popList() {
        if (songList.isNotEmpty()) {
            songList.removeAt(0)
            for (song in songList) {
                song.position -= 1
            }
            sortList()
            //todo update db
        }
    }

    fun getFirstSongUri(): String? {
        if (songList.isNotEmpty()) {
            return songList[0].uri
        } else {
            return null
        }
    }

    fun queueIsNotEmpty(): Boolean {
        return songList.isNotEmpty()
    }

    fun addSongToQueue(uri: String) {
        val position = songList.size
        val newSong = Song(uri, position)
        songList.add(newSong)
        //todo add song to db and get doc id to store on song
    }

    private fun sortList() {
        songList = ArrayList(songList.sortedWith(compareBy {it.position}))
    }
}