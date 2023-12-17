package com.igor_shaula.api_polling.data_layer

import androidx.lifecycle.MutableLiveData

interface TheRepository {

    // MLD = MutableLiveData
    val vehiclesMapMLD get() = MutableLiveData<MutableMap<String, VehicleStatus>>()
    val vehicleDetailsMapMLD get() = MutableLiveData<MutableMap<String, VehicleDetailsRecord>>()

    suspend fun getAllVehiclesIds():MutableMap<String, VehicleStatus>

    fun startGettingVehiclesDetails(size: Int, updateViewModel: () -> Unit)

    fun stopGettingVehiclesDetails()

    fun getNumberOfNearVehicles(): Int
}