package com.igor_shaula.hometask_zf.data

import android.location.Location
import timber.log.Timber

fun detectVehicleStatus(vehicleDetails: VehicleDetailsRecord): VehicleStatus {
    val resultHolder = FloatArray(1)
    Location.distanceBetween(
        vehicleDetails.latitude, vehicleDetails.longitude,
        TARGET_LATITUDE, TARGET_LONGITUDE, resultHolder
    )
    Timber.d("distanceBetween for ${vehicleDetails.vehicleId} is: ${resultHolder[0]} meters")
    return if (resultHolder[0] == 0F) {
        VehicleStatus.IN_PLACE
    } else if (resultHolder[0] < DISTANCE_LIMIT) {
        VehicleStatus.NEAR
    } else {
        VehicleStatus.AFAR
    }
}

fun detectNumberOfNearVehicles(vehicleRecordsList: List<VehicleRecord>?): Int {
    if (vehicleRecordsList.isNullOrEmpty()) {
        return -42
    }
    return vehicleRecordsList.filter {
        it.vehicleStatus == VehicleStatus.IN_PLACE || it.vehicleStatus == VehicleStatus.NEAR
    }.size
}