package com.igor_shaula.api_polling.data_layer.network_data_source

import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.network_data_source.retrofit.VehicleRetrofitNetworkServiceImpl

class NetworkDataSource {

    private val vehicleDataNetworkService = VehicleRetrofitNetworkServiceImpl()
//    private val vehicleDataNetworkService = VehicleStubNetworkServiceImpl()

    suspend fun readVehiclesList(): List<VehicleRecord> =
        vehicleDataNetworkService.getVehiclesList().toVehicleItemRecords()

    suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        vehicleDataNetworkService.getVehicleDetails(vehicleId)?.toVehicleItemRecords()
}
