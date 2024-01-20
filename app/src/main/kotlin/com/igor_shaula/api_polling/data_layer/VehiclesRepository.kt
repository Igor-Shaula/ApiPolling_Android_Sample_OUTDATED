package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData

interface VehiclesRepository {

    val mainDataStorage: MutableLiveData<MutableMap<String, VehicleRecord>>

    suspend fun launchGetAllVehicleIdsRequest(toggleMainBusyState: (Boolean) -> Unit)

    suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleRecord>?,
        updateViewModel: (String, VehicleDetailsRecord) -> Unit,
        toggleBusyStateFor: (String, Boolean) -> Unit
    )

    fun stopGettingVehiclesDetails()

    fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleRecord>?): Int
}
