package com.igor_shaula.api_polling.data_layer

data class VehicleRecord(
    val vehicleId: String,
    val vehicleStatus: VehicleStatus,
    val isBusyGettingNewData: Boolean = false,
    val currentLocation: CurrentLocation? = null
)

data class CurrentLocation(
    val latitude: Double,
    val longitude: Double
)

enum class VehicleStatus(val uiStatus: String, val isNear: Boolean?) {
    UNKNOWN("unknown", null),
    NEAR("near", true),
    AFAR("far away", false),
    IN_PLACE("in place", true),
    NEW_ROUND("new round", null)
}
