package com.igor_shaula.api_polling.data_layer.network

import com.igor_shaula.api_polling.data_layer.AbstractRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.network.retrofit.VehicleNetworkServiceImpl
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords

class NetworkRepositoryImpl : AbstractRepository() {

    private val vehicleDataNetworkService = VehicleNetworkServiceImpl()

    override suspend fun readVehiclesList(): List<VehicleRecord> =
        vehicleDataNetworkService.getVehiclesList().body().toVehicleItemRecords()

    override suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        vehicleDataNetworkService.getVehicleDetails(vehicleId).body()?.toVehicleItemRecords()
}
