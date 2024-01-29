package com.igor_shaula.api_polling.data_layer.data_sources

import com.igor_shaula.api_polling.data_layer.CurrentLocation
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehicleRetrofitNetworkServiceImpl

// this class should consume several different API clients - not only Retrofit
class NetworkDataSource(
    private val vehicleDataNetworkService: VehicleRetrofitNetworkServiceImpl
    // add other network API clients as dependencies here
) {

    suspend fun readVehiclesListResult(): Result<List<VehicleRecord>> =
        vehicleDataNetworkService.getVehiclesListResult().toVehicleItemRecordsResult()

    suspend fun getVehicleDetailsResult(vehicleId: String): CurrentLocation? =
        vehicleDataNetworkService.getVehicleDetailsResult(vehicleId).toVehicleItemRecords()
}
