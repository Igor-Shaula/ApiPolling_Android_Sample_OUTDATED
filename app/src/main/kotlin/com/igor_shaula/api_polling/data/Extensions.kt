package com.igor_shaula.api_polling.data

import com.igor_shaula.api_polling.data.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data.network.VehicleModel

fun List<Pair<String, VehicleStatus>>.toVehicleRecordList(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this.forEach {
        result.add(VehicleRecord(it.first, it.second))
    }
    return result
}

fun List<VehicleModel>?.toVehicleItemRecords(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this?.forEach {
        result.add(VehicleRecord(vehicleId = it.vehicleId, vehicleStatus = VehicleStatus.UNKNOWN))
    }
    return result
}

fun VehicleDetailsModel.toVehicleItemRecords(): VehicleDetailsRecord =
    VehicleDetailsRecord(
        vehicleId = this.vehicleId,
        latitude = this.location.latitude,
        longitude = this.location.longitude
    )
