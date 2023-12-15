package com.igor_shaula.api_polling.data

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus
)

enum class VehicleStatus {
    UNKNOWN, NEAR, AFAR, IN_PLACE
}

data class VehicleDetailsRecord(
    val vehicleId: String,
    val latitude: Double,
    val longitude: Double
)
