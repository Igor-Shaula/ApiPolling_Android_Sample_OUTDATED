package com.igor_shaula.api_polling.data_layer.network_data_source

import com.igor_shaula.api_polling.data_layer.AbstractVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.network_data_source.retrofit.VehicleRetrofitNetworkServiceImpl
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords

class NetworkRepositoryImpl : AbstractVehiclesRepository() {

    private val vehicleDataNetworkService = VehicleRetrofitNetworkServiceImpl()
//    private val vehicleDataNetworkService = VehicleStubNetworkServiceImpl()

    override suspend fun readVehiclesList(): List<VehicleRecord> =
        vehicleDataNetworkService.getVehiclesList().toVehicleItemRecords()

    override suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? =
        vehicleDataNetworkService.getVehicleDetails(vehicleId)?.toVehicleItemRecords()
}
