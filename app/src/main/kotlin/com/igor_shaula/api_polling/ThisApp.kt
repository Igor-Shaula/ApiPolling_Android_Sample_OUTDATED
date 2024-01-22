package com.igor_shaula.api_polling

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.igor_shaula.api_polling.data_layer.AbstractVehiclesRepository
import com.igor_shaula.api_polling.data_layer.network_data_source.NetworkRepositoryImpl
import com.igor_shaula.api_polling.data_layer.stub_data_source.StubRepositoryImpl
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


    enum class RepositoryType {
        NETWORK, STUB
    }

    companion object {
        private val networkRepo: NetworkRepositoryImpl by lazy {
            NetworkRepositoryImpl()
        }
        private val stubRepo: StubRepositoryImpl by lazy {
            StubRepositoryImpl()
        }
        private lateinit var currRepo: AbstractVehiclesRepository

        fun getRepository() : AbstractVehiclesRepository {
            if (!this::currRepo.isInitialized) {
                currRepo = networkRepo;
            }
            return currRepo;
        }

        /**
         * Switches the Repository impl between Network and Stub
         */
        fun switchRepositoryBy(type: RepositoryType) : AbstractVehiclesRepository {
            currRepo = when(type) {
                RepositoryType.STUB -> stubRepo
                RepositoryType.NETWORK -> networkRepo
            }
            return currRepo;
        }
    }
}
