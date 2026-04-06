package com.iqbalwork.ramadhancamp.shared.di

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AlarmeeSchedulerAndroid
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.N)
actual fun platformModule(): Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<AlarmeeScheduler> {
        AlarmeeSchedulerAndroid(
            context = get<Context>(),
            configuration = AlarmeeAndroidPlatformConfiguration(
                notificationIconResId = android.R.drawable.ic_dialog_info,
                notificationChannels = listOf(
                    AlarmeeNotificationChannel(
                        id = "pray_notifications",
                        name = "Prayer Time",
                        importance = NotificationManager.IMPORTANCE_HIGH,
                    )
                )
            )
        )
    }
}