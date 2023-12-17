package com.igor_shaula.api_polling.ui_layer.list_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class VehicleListViewModel : ViewModel() {

    private var repository: TheRepository = ThisApp.getVehiclesRepository()

    // MLD = MutableLiveData
    val vehiclesMapMLD by lazy { MutableLiveData<MutableMap<String, VehicleStatus>>() }

    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()

    fun getAllVehiclesIds() {
        MainScope().launch {
            vehiclesMapMLD.value = repository.getAllVehiclesIds()
            Timber.i("vehiclesMap.value = ${vehiclesMapMLD.value}")
        }
    }

    fun startGettingVehiclesDetails(size: Int) {
        repository.startGettingVehiclesDetails(size, ::updateTheViewModel)
    }

    fun stopGettingVehiclesDetails() {
        repository.stopGettingVehiclesDetails()
    }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles()

    private fun updateTheViewModel() {
        timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
    }
}
