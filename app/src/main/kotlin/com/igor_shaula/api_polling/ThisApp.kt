package com.igor_shaula.api_polling

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.network.NetworkRepositoryImpl
import com.igor_shaula.api_polling.data_layer.stub_source.StubRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

val TIME_TO_SHOW_GOTO_STUB_DIALOG = booleanPreferencesKey("timeToShowGoToStubDialog")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "localDataStore")

class ThisApp : Application() {

    override fun onCreate() {
        enableStrictMode()
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    // why we need this - https://developer.android.com/reference/android/os/StrictMode.html
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
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

    companion object {

        // simplest ever implementation of DI - popular solutions will be added later
        fun getVehiclesRepository(): VehiclesRepository = NetworkRepositoryImpl()

        // i decided for now to just create the needed type and avoid complexity of logic here
        fun getStubVehiclesRepository(): VehiclesRepository = StubRepositoryImpl()
    }
}
