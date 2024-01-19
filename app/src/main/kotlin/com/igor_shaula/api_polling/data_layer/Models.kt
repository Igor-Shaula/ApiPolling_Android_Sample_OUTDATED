package com.igor_shaula.api_polling.data_layer

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus,
    val isBusyGettingNewData: Boolean = false
)

enum class VehicleStatus(val uiStatus: String, val isNear: Boolean?) {
    UNKNOWN("unknown", null),
    NEAR("near", true),
    AFAR("far away", false),
    IN_PLACE("in place", true),
    NEW_ROUND("new round", null)
}

data class VehicleDetailsRecord(
    val vehicleId: String,
    val latitude: Double,
    val longitude: Double
)
