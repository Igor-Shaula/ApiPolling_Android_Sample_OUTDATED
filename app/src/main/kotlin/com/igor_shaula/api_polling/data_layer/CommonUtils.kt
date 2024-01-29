package com.igor_shaula.api_polling.data_layer

import android.location.Location

fun detectVehicleStatus(vehicleDetails: CurrentLocation): VehicleStatus {
    val resultHolder = FloatArray(1)
    Location.distanceBetween(
        vehicleDetails.latitude, vehicleDetails.longitude,
        TARGET_LATITUDE, TARGET_LONGITUDE, resultHolder
    )
    return if (resultHolder[0] == 0F) {
        VehicleStatus.IN_PLACE
    } else if (resultHolder[0] < NEAR_DISTANCE_LIMIT) {
        VehicleStatus.NEAR
    } else {
        VehicleStatus.AFAR
    }
}

fun detectNumberOfNearVehicles(vehicleRecordsList: List<VehicleRecord>?): Int {
    if (vehicleRecordsList.isNullOrEmpty()) {
        return FAKE_FOR_DEFAULT_NUMBER_OF_NEAR_VEHICLES
    }
    return vehicleRecordsList.filter {
        it.vehicleStatus == VehicleStatus.IN_PLACE || it.vehicleStatus == VehicleStatus.NEAR
    }.size
}