package com.igor_shaula.hometask_zf

import android.app.Application
import timber.log.Timber

class ThisApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.tag("shaula"))
//        Timber.plant(Timber.DebugTree())
    }
}