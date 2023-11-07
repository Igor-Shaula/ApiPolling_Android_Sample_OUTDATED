package com.igor_shaula.hometask_zf.data

import com.igor_shaula.hometask_zf.network.VehicleNetworkServiceImpl

class VehiclesRepository {

    suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleList = vehicleDataNetworkService.getVehiclesList()
        return vehicleList.body().toVehicleItemRecords()
    }

    suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord? {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleDetails = vehicleDataNetworkService.getVehicleDetails(vehicleId)
        return vehicleDetails.body()?.toVehicleItemRecords()
    }
}
