package com.igor_shaula.api_polling

import android.app.Application
import android.os.StrictMode
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.data_sources.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.FakeDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehicleRetrofitNetworkServiceImpl
import timber.log.Timber

//val TIME_TO_SHOW_GOTO_FAKE_DIALOG = booleanPreferencesKey("timeToShowGoToFakeDialog")

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "localDataStore")

class ThisApp : Application() {

    override fun onCreate() {
        StrictMode.enableDefaults() // https://developer.android.com/reference/android/os/StrictMode.html
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun getRepository(): VehiclesRepository = Companion.getRepository()

//    fun readNeedFakeDialogFromLocalPrefs(): Flow<Boolean> =
//        dataStore.data.map { preferences ->
//            preferences[TIME_TO_SHOW_GOTO_FAKE_DIALOG] ?: false
//        }

//    suspend fun saveNeedFakeDialogToLocalPrefs(showFakeDataNextTime: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[TIME_TO_SHOW_GOTO_FAKE_DIALOG] = showFakeDataNextTime
//        }
//    }

    enum class ActiveDataSource {
        NETWORK, FAKE
    }

    companion object {

// TODO: current solution is TEMPORARY - later move all DataSource usage logic into the Repository level

        private val networkDataRepository: VehiclesRepository by lazy {
            VehiclesRepository(
                NetworkDataSource(VehicleRetrofitNetworkServiceImpl()),
                FakeDataSource(),
                ActiveDataSource.NETWORK
            )
        }

        private val fakeDataRepository: VehiclesRepository by lazy {
            VehiclesRepository(
                NetworkDataSource(VehicleRetrofitNetworkServiceImpl()),
                FakeDataSource(),
                ActiveDataSource.FAKE
            )
        }

        private lateinit var currentRepository: VehiclesRepository

        fun getRepository(): VehiclesRepository {
            if (!this::currentRepository.isInitialized) {
                currentRepository = networkDataRepository
            }
            return currentRepository
        }

        /**
         * Switches the DataSource for the VehiclesRepository between Network and Fake
         */
        fun switchActiveDataSource(type: ActiveDataSource): VehiclesRepository {
            currentRepository = when (type) {
                ActiveDataSource.FAKE -> fakeDataRepository
                ActiveDataSource.NETWORK -> networkDataRepository
            }
            return currentRepository
        }
    }
}
