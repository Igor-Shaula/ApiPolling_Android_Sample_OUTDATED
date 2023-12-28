package com.igor_shaula.api_polling.data_layer

interface VehiclesRepository {

    suspend fun getAllVehiclesIds(): MutableMap<String, VehicleRecord>

    suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleRecord>?,
        updateViewModel: (String, VehicleDetailsRecord) -> Unit
    )

    fun stopGettingVehiclesDetails()

    fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleRecord>?): Int
}
