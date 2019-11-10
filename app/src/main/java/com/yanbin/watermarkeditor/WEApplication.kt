package com.yanbin.watermarkeditor

import android.app.Application
import com.enginebai.gallery.di.repoModule
import com.enginebai.gallery.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WEApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WEApplication)
            modules(listOf(
                viewModelModule,
                repoModule
            ))
        }
    }
}
