package com.igor_shaula.hometask_zf.data

import android.location.Location
import timber.log.Timber

fun detectVehicleStatus(vehicleDetails: VehicleDetailsRecord): VehicleStatus {
    val floatArrayResult = FloatArray(1)
    Location.distanceBetween(
        vehicleDetails.latitude, vehicleDetails.longitude,
        TARGET_LATITUDE, TARGET_LONGITUDE, floatArrayResult
    )
    Timber.d("distanceBetween for ${vehicleDetails.vehicleId} is: ${floatArrayResult[0]} meters")
    val isNear = floatArrayResult[0] < DISTANCE_LIMIT
    return if (isNear) VehicleStatus.NEAR else VehicleStatus.AFAR
}