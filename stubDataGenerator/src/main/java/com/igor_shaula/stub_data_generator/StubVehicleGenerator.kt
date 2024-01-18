package com.igor_shaula.stub_data_generator

// TODO this class should produce data similar to what is coming over network - in String/JSON type

class StubVehicleGenerator(
    private val vehicleIdBase: String,
//    private val neededQuantity: Int
) {
    private var currentVehicleIndex = 0

    fun createNextVehicleModelString() = vehicleIdBase + currentVehicleIndex++
}