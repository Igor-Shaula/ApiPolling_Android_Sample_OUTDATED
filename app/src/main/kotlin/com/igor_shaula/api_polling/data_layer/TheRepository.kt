package com.igor_shaula.api_polling.data_layer

import kotlin.coroutines.CoroutineContext

interface TheRepository {

    suspend fun getAllVehiclesIds(): MutableMap<String, VehicleStatus>

    suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleStatus>?,
        updateViewModel: (Pair<String, VehicleDetailsRecord>) -> Unit,
        coroutineContext: CoroutineContext
    )

    fun stopGettingVehiclesDetails()

    fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleStatus>?): Int
}
