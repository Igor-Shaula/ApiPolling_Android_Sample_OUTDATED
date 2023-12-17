package com.igor_shaula.api_polling.data_layer.network

import com.igor_shaula.api_polling.data_layer.JavaTPEBasedPollingEngine
import com.igor_shaula.api_polling.data_layer.PollingEngine
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.detectNumberOfNearVehicles
import com.igor_shaula.api_polling.data_layer.detectVehicleStatus
import com.igor_shaula.api_polling.data_layer.network.retrofit.VehicleNetworkServiceImpl
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords
import com.igor_shaula.api_polling.data_layer.toVehicleRecordList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class TheRepositoryNetworkImpl : TheRepository {

    private var pollingEngine: PollingEngine? = null

    override suspend fun getAllVehiclesIds(): MutableMap<String, VehicleStatus> =
        readVehiclesList()
            .associateBy({ it.vehicleId }, { it.vehicleStatus })
            .toMutableMap()

    override fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleStatus>?,
        updateViewModel: (Pair<String, VehicleStatus>) -> Unit
    ) {
        vehiclesMap?.let {
            prepareThreadPoolExecutor(it.size)
            vehiclesMap.forEach { (key, _) ->
                pollingEngine?.launch({ getAllDetailsForOneVehicle(key, updateViewModel) }, 5)
            }
        }
    }

    override fun stopGettingVehiclesDetails() {
        pollingEngine?.stopAndClearItself()
    }

    override fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleStatus>?): Int {
        val vehicleRecordsList = vehiclesMap?.toList()?.toVehicleRecordList()
        return detectNumberOfNearVehicles(vehicleRecordsList)
    }

    private suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleList = vehicleDataNetworkService.getVehiclesList()
        return vehicleList.body().toVehicleItemRecords()
    }

    private fun prepareThreadPoolExecutor(size: Int) {
        pollingEngine = JavaTPEBasedPollingEngine.prepare(size)
    }

    private fun getAllDetailsForOneVehicle(
        vehicleId: String, updateViewModel: (Pair<String, VehicleStatus>) -> Unit
    ) {
        MainScope().launch {
            val vehicleDetails = readVehicleDetails(vehicleId)
            Timber.d("vehicleDetails = $vehicleDetails")
            if (vehicleDetails != null) {
                updateVehicleDetailsMap(vehicleId, vehicleDetails)
                val pair = Pair(vehicleDetails.vehicleId, detectVehicleStatus(vehicleDetails))
                updateViewModel(pair)
            }
        }
    }

    private suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleDetails = vehicleDataNetworkService.getVehicleDetails(vehicleId)
        return vehicleDetails.body()?.toVehicleItemRecords()
    }

    private fun updateVehicleDetailsMap(key: String, value: VehicleDetailsRecord) {
        if (vehicleDetailsMapMLD.value == null) {
            // we need this preparation for value?.put() to work as the value is nullable
            vehicleDetailsMapMLD.value = mutableMapOf()
        }
        vehicleDetailsMapMLD.value?.put(key, value)
        vehicleDetailsMapMLD.value = vehicleDetailsMapMLD.value
        Timber.d("vehicleDetailsMapMLD.value = ${vehicleDetailsMapMLD.value}")
    }
}
