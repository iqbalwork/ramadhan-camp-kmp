package com.iqbalwork.ramadhancamp.shared.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<HttpClientEngine> { Darwin.create() }
    factory<com.iqbalwork.ramadhancamp.shared.common.media.AudioPlayer> { com.iqbalwork.ramadhancamp.shared.common.media.IosAudioPlayer() }
    factory<com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager> { com.iqbalwork.ramadhancamp.shared.common.utils.IosShareManager() }
}