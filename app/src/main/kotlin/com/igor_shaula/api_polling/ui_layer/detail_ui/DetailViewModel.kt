package com.igor_shaula.api_polling.ui_layer.detail_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus

class DetailViewModel : ViewModel() {

    private var repository: TheRepository = ThisApp.getVehiclesRepository()

    val vehiclesMap by lazy { MutableLiveData<MutableMap<String, VehicleStatus>>() }

    val vehiclesDetailsMap by lazy { MutableLiveData<MutableMap<String, VehicleDetailsRecord>>() }

    fun getNumberOfNearVehicles() = repository.getNumberOfNearVehicles(vehiclesMap.value)
}