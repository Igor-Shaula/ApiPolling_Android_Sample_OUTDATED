package com.igor_shaula.hometask_zf.data

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus
)

enum class VehicleStatus {
    CLOSE, FAR_AWAY
}
