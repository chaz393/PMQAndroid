package com.partymusicq.android.util

class UtilPlayer {

    companion object {
        fun msToFormattedTime(ms: Long): String {
            val sec = (ms/1000)%60
            val min = (ms/1000/60)%60
            return String.format("%02d:%02d", min, sec)
        }
    }
}