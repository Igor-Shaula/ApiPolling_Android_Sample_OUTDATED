package com.igor_shaula.api_polling.data_layer

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus,
    val isBusyGettingNewData: Boolean = false
)

enum class VehicleStatus {
    UNKNOWN, NEAR, AFAR, IN_PLACE, NEW_ROUND
}

data class VehicleDetailsRecord(
    val vehicleId: String,
    val latitude: Double,
    val longitude: Double
)
