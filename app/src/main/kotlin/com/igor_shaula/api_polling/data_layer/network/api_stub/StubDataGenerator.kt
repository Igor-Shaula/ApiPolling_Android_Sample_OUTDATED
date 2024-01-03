package com.igor_shaula.api_polling.data_layer.network.api_stub

import com.igor_shaula.api_polling.data_layer.network.VehicleModel

class StubVehicleGenerator(
    private val vehicleIdBase: String,
//    private val neededQuantity: Int
) {
    private var currentVehicleIndex = 0

    fun createNextVehicleModel() = VehicleModel(vehicleIdBase + currentVehicleIndex++)
}