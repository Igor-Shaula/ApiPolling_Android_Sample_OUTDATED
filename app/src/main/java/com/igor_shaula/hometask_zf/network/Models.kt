package com.igor_shaula.hometask_zf.network

import com.google.gson.annotations.SerializedName

data class VehicleModel(@SerializedName(SERIALIZATION_KEY_VEHICLE_ID) val vehicleId: String)

data class VehicleDetailsModel(
    @SerializedName(SERIALIZATION_KEY_VEHICLE_ID) val vehicleId: String,
    @SerializedName(SERIALIZATION_KEY_LOCATION) val location: LocationModel
)

data class LocationModel(
    @SerializedName(SERIALIZATION_KEY_LATITUDE) val latitude: Double,
    @SerializedName(SERIALIZATION_KEY_LONGITUDE) val longitude: Double
)
