package com.igor_shaula.api_polling

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.network.NetworkRepositoryImpl
import com.igor_shaula.api_polling.data_layer.stub_source.StubRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

val TIME_TO_SHOW_GOTO_STUB_DIALOG = booleanPreferencesKey("timeToShowGoToStubDialog")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "localDataStore")

class ThisApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun readNeedStubDialogFromLocalPrefs(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[TIME_TO_SHOW_GOTO_STUB_DIALOG] ?: false
        }

    fun saveNeedStubDialogToLocalPrefs() {
        MainScope().launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[TIME_TO_SHOW_GOTO_STUB_DIALOG] = true
            }
        }
    }

    companion object {

        // simplest ever implementation of DI - popular solutions will be added later
        fun getVehiclesRepository(): VehiclesRepository = NetworkRepositoryImpl()

        // i decided for now to just create the needed type and avoid complexity of logic here
        fun getStubVehiclesRepository(): VehiclesRepository = StubRepositoryImpl()
    }
}
