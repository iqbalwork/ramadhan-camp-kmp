package com.iqbalwork.ramadhancamp.shared.common.media

import platform.AVFoundation.*
import platform.Foundation.NSURL

class IosAudioPlayer : AudioPlayer {
    private var player: AVPlayer? = null
    override fun play(url: String) {
        val nsUrl = NSURL(string = url)
        player = AVPlayer(uRL = nsUrl).apply { play() }
    }
    override fun pause() { player?.pause() }
    override fun stop() {
        player?.pause()
        player = null
    }
}
