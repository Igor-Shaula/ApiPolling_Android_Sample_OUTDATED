package com.igor_shaula.hometask_zf.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor_shaula.hometask_zf.data.VehicleStatus
import com.igor_shaula.hometask_zf.data.VehiclesRepository
import com.igor_shaula.hometask_zf.data.detectVehicleStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class VehicleListViewModel : ViewModel() {

    val vehiclesMapMLD = MutableLiveData<MutableMap<String, VehicleStatus>>()

    val timeToUpdateVehicleStatus = MutableLiveData<Unit>()

    private var repository: VehiclesRepository = VehiclesRepository()

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    fun getAllVehiclesIds() {
        MainScope().launch {
            vehiclesMapMLD.value = repository.readVehiclesList()
                .associateBy({ it.vehicleId }, { it.vehicleStatus }).toMutableMap()
            Timber.d("vehiclesMapMLD = ${vehiclesMapMLD.value}")
        }
    }

    fun startGettingVehiclesDetails(size: Int) {
        if (vehiclesMapMLD.value == null) return
        prepareThreadPoolExecutor(size)
        vehiclesMapMLD.value?.forEach { (key, _) ->
            scheduledThreadPoolExecutor?.scheduleAtFixedRate(
                { getAllDetailsForOneVehicle(key) }, 0, 5, TimeUnit.SECONDS
            )
        }
    }

//    fun stopGettingVehiclesDetails() {
//        scheduledThreadPoolExecutor?.shutdown()
//    }

    private fun prepareThreadPoolExecutor(size: Int) {
        if (scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor?.isShutdown == true) {
            scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(size)
        }
    }

    private fun getAllDetailsForOneVehicle(vehicleId: String) {
        MainScope().launch {
            val vehicleDetails = repository.readVehicleDetails(vehicleId)
            Timber.d("vehicleDetails = $vehicleDetails")
            if (vehicleDetails != null) {
                vehiclesMapMLD.value?.put(
                    vehicleDetails.vehicleId, detectVehicleStatus(vehicleDetails)
                )
                timeToUpdateVehicleStatus.value = Unit // just to show new statuses on UI
            }
        }
    }
}
