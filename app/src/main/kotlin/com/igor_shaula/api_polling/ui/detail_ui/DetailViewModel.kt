package com.igor_shaula.api_polling.ui.detail_ui

import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data.VehiclesRepository

class DetailViewModel : ViewModel() {

    private var repository: VehiclesRepository = ThisApp.getVehiclesRepository()

    val vehiclesDetailsMap = repository.vehicleDetailsMapMLD

    val vehiclesMap = repository.vehiclesMapMLD

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles()
}