package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.data_sources.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.FakeDataSource
import com.igor_shaula.api_polling.data_layer.polling_engines.JavaTPEBasedPollingEngine
import com.igor_shaula.api_polling.data_layer.polling_engines.PollingEngine
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class DefaultVehiclesRepository(
    private val networkDataSource: NetworkDataSource,
    private val fakeDataSource: FakeDataSource,
    private val activeDataSource: ThisApp.ActiveDataSource,
    private var pollingEngine: PollingEngine? = null
) : VehiclesRepository {

    override val mainDataStorage = MutableLiveData<MutableMap<String, VehicleRecord>>()

    var vehiclesDataFlow: Flow<MutableMap<String, VehicleRecord>?> = flowOf(mainDataStorage.value)

    private lateinit var coroutineScope: CoroutineScope // lateinit is not dangerous here

    init {
        createThisCoroutineScope()
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

    // region private methods

    private fun createThisCoroutineScope() {
        coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)
    }

    private suspend fun readVehiclesList(): List<VehicleRecord> =
        if (activeDataSource == ThisApp.ActiveDataSource.NETWORK) {
            networkDataSource.readVehiclesList()
        } else {
            fakeDataSource.readVehiclesList()
        }

    private fun preparePollingEngine(size: Int) {
        pollingEngine = JavaTPEBasedPollingEngine.prepare(size)
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

    private suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        if (activeDataSource == ThisApp.ActiveDataSource.NETWORK) {
            networkDataSource.readVehicleDetails(vehicleId) // may be nullable due to Retrofit
        } else {
            fakeDataSource.readVehicleDetails(vehicleId)
        }

    // endregion private methods
}
