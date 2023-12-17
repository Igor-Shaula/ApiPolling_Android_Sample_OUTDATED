package com.igor_shaula.api_polling.ui.list_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data.VehiclesRepository

class VehicleListViewModel : ViewModel() {

    private var repository: VehiclesRepository = ThisApp.getVehiclesRepository()

    val vehiclesMap = repository.vehiclesMapMLD

    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()

    fun getAllVehiclesIds() {
        repository.getAllVehiclesIds()
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
