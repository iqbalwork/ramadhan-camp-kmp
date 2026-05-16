package com.iqbalwork.ramadhancamp.shared.common.audio.di

import com.iqbalwork.ramadhancamp.shared.common.audio.AudioController
import org.koin.dsl.module

val audioModule = module {
    single { AudioController() }
}
