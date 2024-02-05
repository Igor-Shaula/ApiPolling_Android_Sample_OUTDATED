package com.igor_shaula.api_polling.data_layer.data_sources

import androidx.lifecycle.Observer
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.DefaultVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RepositoryProperty(
    private val vehiclesMapObserver: Observer<MutableMap<String, VehicleRecord>>,
    private val mainErrorStateInfoObserver: Observer<String?>
) : ReadWriteProperty<Any, DefaultVehiclesRepository> {

    private lateinit var repository: DefaultVehiclesRepository

    override fun getValue(thisRef: Any, property: KProperty<*>): DefaultVehiclesRepository {
        if (!this::repository.isInitialized) {
            repository = ThisApp.getRepository()
            repository.mainDataStorage.observeForever(vehiclesMapObserver)
            repository.mainErrorStateInfo.observeForever(mainErrorStateInfoObserver)
        }
        return repository
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: DefaultVehiclesRepository) {
        if (this::repository.isInitialized) {
            repository.mainDataStorage.removeObserver(vehiclesMapObserver)
            repository.mainErrorStateInfo.removeObserver(mainErrorStateInfoObserver)
        }
        repository = value
        repository.mainDataStorage.observeForever(vehiclesMapObserver)
        repository.mainErrorStateInfo.observeForever(mainErrorStateInfoObserver)
    }
}