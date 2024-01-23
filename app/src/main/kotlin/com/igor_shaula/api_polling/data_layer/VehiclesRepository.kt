package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.data_sources.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.StubDataSource
import com.igor_shaula.api_polling.data_layer.polling_engines.JavaTPEBasedPollingEngine
import com.igor_shaula.api_polling.data_layer.polling_engines.PollingEngine
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
    private val activeDataSource: ThisApp.ActiveDataSource,
    private var pollingEngine: PollingEngine? = null
) : VehiclesRepositoryContract {

    override val mainDataStorage = MutableLiveData<MutableMap<String, VehicleRecord>>()

//    val data: Flow<Map.Entry<String, VehicleRecord>>
//        get() {
//            TODO()
//        }

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
        updateViewModel: (String, VehicleDetailsRecord) -> Unit,
        toggleBusyStateFor: (String, Boolean) -> Unit
    ) {
        mainDataStorage.value?.let {
            preparePollingEngine(it.size)
            it.forEach { (key, _) ->
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

    override fun getNumberOfNearVehicles(): Int {
        val vehicleRecordsList = mainDataStorage.value?.toList()?.toVehicleRecordList()
        return detectNumberOfNearVehicles(vehicleRecordsList)
    }

    override fun getNumberOfAllVehicles(): Int = mainDataStorage.value?.size ?: 0

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
