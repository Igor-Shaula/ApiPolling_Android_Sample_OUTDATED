package com.igor_shaula.hometask_zf.ui.detail

import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.ThisApp
import com.igor_shaula.hometask_zf.data.VehiclesRepository

class DetailViewModel : ViewModel() {

    private var repository: VehiclesRepository = ThisApp.getVehiclesRepository()

    val vehiclesDetailsMap = repository.vehicleDetailsMapMLD

    val vehiclesMap = repository.vehiclesMapMLD
}