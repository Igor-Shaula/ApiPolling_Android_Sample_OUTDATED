package com.igor_shaula.api_polling

import android.app.Application
import com.igor_shaula.api_polling.data.TheRepositoryImpl
import timber.log.Timber

class ThisApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {

        // vehiclesRepository cannot be not lateinit because it actually can be null
        private var vehiclesRepository: TheRepositoryImpl? = null

        fun getVehiclesRepository(): TheRepositoryImpl {
            if (vehiclesRepository == null) {
                vehiclesRepository = TheRepositoryImpl()
            }
            return vehiclesRepository as TheRepositoryImpl
        }
    }
}