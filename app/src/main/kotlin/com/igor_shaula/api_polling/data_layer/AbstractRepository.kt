package com.igor_shaula.api_polling.data_layer

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class AbstractRepository : TheRepository {

    private var pollingEngine: PollingEngine? = null

    private val coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)

    override suspend fun getAllVehiclesIds(): MutableMap<String, VehicleStatus> =
        readVehiclesList()
            .associateBy({ it.vehicleId }, { it.vehicleStatus })
            .toMutableMap()

    override suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleStatus>?,
        updateViewModel: (Pair<String, VehicleDetailsRecord>) -> Unit
    ) {
        vehiclesMap?.let {
            preparePollingEngine(it.size)
            vehiclesMap.forEach { (key, _) ->
                pollingEngine?.launch(DEFAULT_POLLING_INTERVAL) {
                    coroutineScope.launch {
                        getAllDetailsForOneVehicle(key, updateViewModel)
                    }
                }
            }
        }
    }

    override fun stopGettingVehiclesDetails() {
        pollingEngine?.stopAndClearItself()
        coroutineScope.cancel()
    }

    override fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleStatus>?): Int {
        val vehicleRecordsList = vehiclesMap?.toList()?.toVehicleRecordList()
        return detectNumberOfNearVehicles(vehicleRecordsList)
    }

    protected abstract suspend fun readVehiclesList(): List<VehicleRecord>

    protected abstract suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord?

    private suspend fun getAllDetailsForOneVehicle(
        vehicleId: String, updateViewModel: (Pair<String, VehicleDetailsRecord>) -> Unit
    ) {
        val vehicleDetails = readVehicleDetails(vehicleId)
        Timber.d("vehicleDetails = $vehicleDetails")
        if (vehicleDetails != null) {
            updateViewModel(Pair(vehicleId, vehicleDetails))
        }
    }

    private fun preparePollingEngine(size: Int) {
        pollingEngine = JavaTPEBasedPollingEngine.prepare(size)
    }
}
