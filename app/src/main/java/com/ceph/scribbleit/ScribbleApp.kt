package com.ceph.scribbleit

import android.app.Application
import com.ceph.scribbleit.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScribbleApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
            androidContext(this@ScribbleApp)
        }
    }
}