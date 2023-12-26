package com.igor_shaula.api_polling

import android.app.Application
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.stub_source.StubRepositoryImpl
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
        private var vehiclesRepository: TheRepository? = null

        // simplest ever implementation of DI - popular solutions will be added later
        fun getVehiclesRepository(): TheRepository {
            if (vehiclesRepository == null) {
//                vehiclesRepository = NetworkRepositoryImpl()
                vehiclesRepository = StubRepositoryImpl()
            } // else add some debug source of data, maybe based on coroutines
            return vehiclesRepository as TheRepository
        }
    }
}