package com.iqbalwork.ramadhancamp.shared.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.iqbalwork.ramadhancamp.shared.common.utils.AndroidShareManager
import com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.N)
actual fun platformModule(): Module = module {
    single<HttpClientEngine> { OkHttp.create() }

    factory<ShareManager> { AndroidShareManager(get()) }
}
