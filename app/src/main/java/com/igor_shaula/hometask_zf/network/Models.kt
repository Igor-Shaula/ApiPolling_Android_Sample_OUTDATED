package com.igor_shaula.hometask_zf.network

import com.google.gson.annotations.SerializedName

data class VehicleModel(@SerializedName(SERIALIZATION_KEY_VEHICLE) val vehicleId: String)