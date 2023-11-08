package com.igor_shaula.hometask_zf.data

import androidx.lifecycle.MutableLiveData
import com.igor_shaula.hometask_zf.network.VehicleNetworkServiceImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class VehiclesRepository {

    val vehiclesMapMLD = MutableLiveData<MutableMap<String, VehicleStatus>>()

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    fun getAllVehiclesIds() {
        MainScope().launch {
            vehiclesMapMLD.value = readVehiclesList()
                .associateBy({ it.vehicleId }, { it.vehicleStatus }).toMutableMap()
            Timber.d("vehiclesMapMLD = ${vehiclesMapMLD.value}")
        }
    }

    private suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleList = vehicleDataNetworkService.getVehiclesList()
        return vehicleList.body().toVehicleItemRecords()
    }

    fun startGettingVehiclesDetails(size: Int, updateViewModel: () -> Unit) {
//        if (vehiclesMapMLD.value == null) return
        prepareThreadPoolExecutor(size)
        vehiclesMapMLD.value?.forEach { (key, _) ->
            scheduledThreadPoolExecutor?.scheduleAtFixedRate(
                { getAllDetailsForOneVehicle(key, updateViewModel) }, 0, 5, TimeUnit.SECONDS
            )
        }
    }

    private fun prepareThreadPoolExecutor(size: Int) {
        if (scheduledThreadPoolExecutor == null || scheduledThreadPoolExecutor?.isShutdown == true) {
            scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(size)
        }
    }

    private fun getAllDetailsForOneVehicle(vehicleId: String, updateViewModel: () -> Unit) {
        MainScope().launch {
            val vehicleDetails = readVehicleDetails(vehicleId)
            Timber.d("vehicleDetails = $vehicleDetails")
            if (vehicleDetails != null) {
                vehiclesMapMLD.value?.put(
                    vehicleDetails.vehicleId, detectVehicleStatus(vehicleDetails)
                )
                updateViewModel()
            }
        }
    }

    private suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleDetails = vehicleDataNetworkService.getVehicleDetails(vehicleId)
        return vehicleDetails.body()?.toVehicleItemRecords()
    }

    fun stopGettingVehiclesDetails() {
        scheduledThreadPoolExecutor?.shutdown()
    }
}
