package com.igor_shaula.api_polling.data_layer.data_sources.fake_api_client

// TODO this class should produce data similar to what is coming over network - in String/JSON type

class FakeVehicleGenerator(private val vehicleIdBase: String) {

    private var currentVehicleIndex = 0

    fun createNextVehicleModelString() = vehicleIdBase + (++currentVehicleIndex)
}
