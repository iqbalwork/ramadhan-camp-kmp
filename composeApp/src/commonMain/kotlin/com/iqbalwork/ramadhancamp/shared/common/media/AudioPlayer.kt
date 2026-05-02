package com.iqbalwork.ramadhancamp.shared.common.media

interface AudioPlayer {
    fun play(url: String)
    fun pause()
    fun stop()
}
