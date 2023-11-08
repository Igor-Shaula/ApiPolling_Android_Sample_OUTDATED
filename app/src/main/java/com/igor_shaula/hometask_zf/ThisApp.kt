package com.igor_shaula.hometask_zf

import android.app.Application
import com.igor_shaula.hometask_zf.data.VehiclesRepository
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
        private var vehiclesRepository: VehiclesRepository? = null

        fun getVehiclesRepository(): VehiclesRepository {
            if (vehiclesRepository == null) {
                vehiclesRepository = VehiclesRepository()
            }
            return vehiclesRepository as VehiclesRepository
        }
    }
}