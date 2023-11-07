package com.igor_shaula.hometask_zf.data

import com.igor_shaula.hometask_zf.network.VehicleNetworkServiceImpl
import com.igor_shaula.hometask_zf.network.VehicleModel

class VehiclesRepository {

    suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataNetworkService = VehicleNetworkServiceImpl()
        val vehicleList = vehicleDataNetworkService.getVehiclesList()
        return vehicleList.body().toVehicleItemRecords()
    }
}

private fun List<VehicleModel>?.toVehicleItemRecords(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this?.forEach {
        result.add(VehicleRecord(vehicleId = it.vehicleId, vehicleStatus = VehicleStatus.FAR_AWAY))
    }
    return result
}
