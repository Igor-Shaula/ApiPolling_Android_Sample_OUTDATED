package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class AbstractVehiclesRepository : VehiclesRepository {

    override val mainDataStorage = MutableLiveData<MutableMap<String, VehicleRecord>>()

    private var pollingEngine: PollingEngine? = null

    private lateinit var coroutineScope: CoroutineScope // lateinit is not dangerous here

    init {
        createThisCoroutineScope()
    }

    private fun createThisCoroutineScope() {
        coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)
    }

    override suspend fun launchGetAllVehicleIdsRequest(toggleMainBusyState: (Boolean) -> Unit) {
        toggleMainBusyState.invoke(true)
        val result = readVehiclesList()
            .also { Timber.v("launchGetAllVehicleIdsRequest() -> readVehiclesList() = $it") }
            .associateBy(
                { it.vehicleId },
                { VehicleRecord(it.vehicleId, it.vehicleStatus) })
            .toMutableMap()
            .also { Timber.v("launchGetAllVehicleIdsRequest() -> MutableMap = $it") }
        mainDataStorage.value = result
        toggleMainBusyState.invoke(false)
        Timber.v("launchGetAllVehicleIdsRequest() -> mainDataStorage.value = ${mainDataStorage.value}")
    }

    override suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleRecord>?,
        updateViewModel: (String, VehicleDetailsRecord) -> Unit,
        toggleBusyStateFor: (String, Boolean) -> Unit
    ) {
        vehiclesMap?.let {
            preparePollingEngine(it.size)
            vehiclesMap.forEach { (key, _) ->
                pollingEngine?.launch(DEFAULT_POLLING_INTERVAL) {
                    if (!coroutineScope.isActive) {
                        createThisCoroutineScope()
                    }
                    coroutineScope.launch {
                        toggleBusyStateFor(key, true)
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

    override fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleRecord>?): Int {
        val vehicleRecordsList = vehiclesMap?.toList()?.toVehicleRecordList()
        return detectNumberOfNearVehicles(vehicleRecordsList)
    }

    protected abstract suspend fun readVehiclesList(): List<VehicleRecord>

    protected abstract suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord?

    private suspend fun getAllDetailsForOneVehicle(
        vehicleId: String, updateViewModel: (String, VehicleDetailsRecord) -> Unit
    ) {
        val vehicleDetails = readVehicleDetails(vehicleId)
        Timber.d("vehicleDetails = $vehicleDetails")
        if (vehicleDetails != null) {
            updateViewModel(vehicleId, vehicleDetails)
        }
    }

    private fun preparePollingEngine(size: Int) {
        pollingEngine = JavaTPEBasedPollingEngine.prepare(size)
    }
}
