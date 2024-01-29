package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData

// for now this thing exists for only description of interface for the consumer (as ViewModel)
interface VehiclesRepository {

    val mainDataStorage: MutableLiveData<MutableMap<String, VehicleRecord>>

    suspend fun launchGetAllVehicleIdsRequest(toggleMainBusyState: (Boolean) -> Unit)

    suspend fun startGettingVehiclesDetails(
        updateViewModel: (String, CurrentLocation) -> Unit,
        toggleBusyStateFor: (String, Boolean) -> Unit
    )

    fun stopGettingVehiclesDetails()

    fun getNumberOfNearVehicles(): Int

    fun getNumberOfAllVehicles(): Int
}
