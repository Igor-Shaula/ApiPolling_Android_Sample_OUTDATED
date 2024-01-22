package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.network_data_source.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.stub_data_source.StubDataSource
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class VehiclesRepository(
    private val networkDataSource: NetworkDataSource,
    private val stubDataSource: StubDataSource,
    private val activeDataSource: ThisApp.ActiveDataSource
) : VehiclesRepositoryContract {

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

    private suspend fun readVehiclesList(): List<VehicleRecord> =
        if (activeDataSource == ThisApp.ActiveDataSource.NETWORK) {
            networkDataSource.readVehiclesList()
        } else {
            stubDataSource.readVehiclesList()
        }

    private suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        if (activeDataSource == ThisApp.ActiveDataSource.NETWORK) {
            networkDataSource.readVehicleDetails(vehicleId) // may be nullable due to Retrofit
        } else {
            stubDataSource.readVehicleDetails(vehicleId)
        }

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
