package com.igor_shaula.api_polling.data_layer.data_sources

import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehicleRetrofitNetworkServiceImpl

// potentially this class can consume several different API clients - not only Retrofit
class NetworkDataSource(
    private val vehicleDataNetworkService: VehicleRetrofitNetworkServiceImpl
) {

    suspend fun readVehiclesList(): List<VehicleRecord> =
        vehicleDataNetworkService.getVehiclesList().toVehicleItemRecords()

    suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        vehicleDataNetworkService.getVehicleDetails(vehicleId)?.toVehicleItemRecords()
}
