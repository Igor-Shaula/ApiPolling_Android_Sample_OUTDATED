package com.igor_shaula.api_polling.ui_layer.list_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.detectVehicleStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class VehicleListViewModel : ViewModel() {

    // MLD = MutableLiveData
    val vehiclesMapMLD by lazy { MutableLiveData<MutableMap<String, VehicleStatus>>() }
    val vehiclesDetailsMap by lazy { MutableLiveData<MutableMap<String, VehicleDetailsRecord>>() }

    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()

    private var repository: TheRepository = ThisApp.getVehiclesRepository()

    fun getAllVehiclesIds() {
        MainScope().launch {
            vehiclesMapMLD.value = repository.getAllVehiclesIds()
            Timber.i("vehiclesMap.value = ${vehiclesMapMLD.value}")
        }
    }

    fun startGettingVehiclesDetails() {
        MainScope().launch {
            repository.startGettingVehiclesDetails(vehiclesMapMLD.value, ::updateTheViewModel)
        }
    }

    fun stopGettingVehiclesDetails() {
        repository.stopGettingVehiclesDetails()
    }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles(vehiclesMapMLD.value)

    private fun updateTheViewModel(pair: Pair<String, VehicleDetailsRecord>) {
        vehiclesMapMLD.value?.put(pair.first, detectVehicleStatus(pair.second))
        vehiclesDetailsMap.value?.put(pair.first, pair.second)
        timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
    }
}
