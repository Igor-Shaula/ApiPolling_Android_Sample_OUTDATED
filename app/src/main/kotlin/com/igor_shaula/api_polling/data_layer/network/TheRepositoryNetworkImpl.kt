package com.igor_shaula.api_polling.data_layer.network

import com.igor_shaula.api_polling.data_layer.AbstractRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.network.retrofit.VehicleNetworkServiceImpl
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords
import timber.log.Timber

class TheRepositoryNetworkImpl : AbstractRepository() {

    override suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        Timber.v("readVehiclesList: created vehicleDataNetworkService: ${vehicleDataNetworkService.hashCode()}")
        val vehicleList = vehicleDataNetworkService.getVehiclesList()
        Timber.v("readVehiclesList: received vehicleList: $vehicleList")
        return vehicleList.body().toVehicleItemRecords()
    }

    override suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleDetails = vehicleDataNetworkService.getVehicleDetails(vehicleId)
        return vehicleDetails.body()?.toVehicleItemRecords()
    }
}
