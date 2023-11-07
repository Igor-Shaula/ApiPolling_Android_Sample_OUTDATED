package com.igor_shaula.hometask_zf.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.data.VehicleStatus
import com.igor_shaula.hometask_zf.data.VehiclesRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class VehicleListViewModel : ViewModel() {

    val vehiclesMapMLD = MutableLiveData<Map<String, VehicleStatus>>()

    private var repository: VehiclesRepository = VehiclesRepository()

    init {
        MainScope().launch {
            vehiclesMapMLD.value = repository.readVehiclesList()
                .associateBy({ it.vehicleId }, { it.vehicleStatus })
            Timber.d("vehiclesMapMLD = ${vehiclesMapMLD.value}")
        }
    }

    fun timeToGetAllDetails(vehicleId: String) {
        MainScope().launch {
            val vehicleDetails = repository.readVehicleDetails(vehicleId)
            Timber.d("vehicleDetails = $vehicleDetails")
//            if (vehicleDetails != null)
//                vehiclesMap[vehicleDetails.vehicleId] = vehicleDetails
//            Timber.d("vehiclesMap = $vehiclesMap")
        }
    }
}
