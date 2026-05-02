package com.iqbalwork.ramadhancamp.shared.common.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class AndroidAudioPlayer(private val context: Context) : AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    override fun play(url: String) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.parse(url))
            prepare()
            start()
        }
    }
    override fun pause() { mediaPlayer?.pause() }
    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
