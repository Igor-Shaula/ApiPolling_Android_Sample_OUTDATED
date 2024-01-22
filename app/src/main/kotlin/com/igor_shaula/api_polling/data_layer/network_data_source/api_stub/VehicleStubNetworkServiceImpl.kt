package com.igor_shaula.api_polling.data_layer.network_data_source.api_stub

import com.igor_shaula.api_polling.data_layer.TARGET_LATITUDE
import com.igor_shaula.api_polling.data_layer.TARGET_LONGITUDE
import com.igor_shaula.api_polling.data_layer.network_data_source.LocationModel
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleModel
import com.igor_shaula.stub_data_generator.StubVehicleGenerator
import kotlin.random.Random
import kotlin.random.nextInt

class VehicleStubNetworkServiceImpl {

    fun getVehiclesList(): List<VehicleModel> {
        Thread.sleep(Random.nextInt(50..5_000).toLong())

        val quantity = 30
        val stubDataGenerator = StubVehicleGenerator("API stub vehicle #")

        val result: MutableList<VehicleModel> = mutableListOf()
        repeat(quantity) {
            result.add(VehicleModel(stubDataGenerator.createNextVehicleModelString()))
        }
        return result
    }

    fun getVehicleDetails(vehicleId: String): VehicleDetailsModel {
        Thread.sleep(Random.nextInt(10..10_000).toLong())

        val randomCoefficient = if (Random.nextBoolean()) 0.001 else -0.001
        val randomShift = randomCoefficient * Random.nextInt(0..10)
        val newLocationModel = LocationModel(
            TARGET_LATITUDE + randomShift, TARGET_LONGITUDE + randomShift
        )
        return VehicleDetailsModel(vehicleId, newLocationModel)
    }
}