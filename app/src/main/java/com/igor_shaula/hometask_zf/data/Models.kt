package com.igor_shaula.hometask_zf.data

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus
)

enum class VehicleStatus {
    CLOSE, FAR_AWAY
}

data class VehicleDetailsRecord(
    val vehicleId: String,
    val latitude: Double,
    val longitude: Double
)
