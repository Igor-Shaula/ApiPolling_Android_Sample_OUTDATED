package com.igor_shaula.api_polling.ui.detail_ui

import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data.TheRepository

class DetailViewModel : ViewModel() {

    private var repository: TheRepository = ThisApp.getVehiclesRepository()

    val vehiclesDetailsMap = repository.vehicleDetailsMapMLD

    val vehiclesMap = repository.vehiclesMapMLD

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles()
}