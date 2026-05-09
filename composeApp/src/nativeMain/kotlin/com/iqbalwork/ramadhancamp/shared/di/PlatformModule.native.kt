package com.iqbalwork.ramadhancamp.shared.di

import com.iqbalwork.ramadhancamp.shared.common.utils.IosShareManager
import com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<HttpClientEngine> { Darwin.create() }
    factory<ShareManager> { IosShareManager() }
}
