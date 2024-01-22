package com.igor_shaula.api_polling

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.data_sources.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.StubDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehicleRetrofitNetworkServiceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

val TIME_TO_SHOW_GOTO_STUB_DIALOG = booleanPreferencesKey("timeToShowGoToStubDialog")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "localDataStore")

class ThisApp : Application() {

    override fun onCreate() {
        StrictMode.enableDefaults() // https://developer.android.com/reference/android/os/StrictMode.html
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun readNeedStubDialogFromLocalPrefs(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[TIME_TO_SHOW_GOTO_STUB_DIALOG] ?: false
        }

    suspend fun saveNeedStubDialogToLocalPrefs(showStubDataNextTime: Boolean) {
        dataStore.edit { preferences ->
            preferences[TIME_TO_SHOW_GOTO_STUB_DIALOG] = showStubDataNextTime
        }
    }

    enum class ActiveDataSource {
        NETWORK, STUB
    }

    companion object {

// TODO: current solution is TEMPORARY - later move all DataSource usage logic into the Repository level

        private val networkDataRepository: VehiclesRepository by lazy {
            VehiclesRepository(
                NetworkDataSource(VehicleRetrofitNetworkServiceImpl()),
                StubDataSource(),
                ActiveDataSource.NETWORK
            )
        }

        private val stubDataRepository: VehiclesRepository by lazy {
            VehiclesRepository(
                NetworkDataSource(VehicleRetrofitNetworkServiceImpl()),
                StubDataSource(),
                ActiveDataSource.STUB
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
         * Switches the DataSource for the VehiclesRepository between Network and Stub
         */
        fun switchActiveDataSource(type: ActiveDataSource): VehiclesRepository {
            currentRepository = when (type) {
                ActiveDataSource.STUB -> stubDataRepository
                ActiveDataSource.NETWORK -> networkDataRepository
            }
            return currentRepository
        }
    }
}
