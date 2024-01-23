package com.igor_shaula.api_polling.data_layer.data_sources

import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus

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
