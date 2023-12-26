package com.igor_shaula.api_polling.data_layer.stub_source

import com.igor_shaula.api_polling.data_layer.TARGET_LATITUDE
import com.igor_shaula.api_polling.data_layer.TARGET_LONGITUDE
import com.igor_shaula.api_polling.data_layer.network.LocationModel
import com.igor_shaula.api_polling.data_layer.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network.VehicleModel
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

class StubDataSource {

    suspend fun getVehiclesList(): List<VehicleModel> {
        delay(1500)
        val result: MutableList<VehicleModel> = mutableListOf()
        result.add(VehicleModel("id-1"))
        result.add(VehicleModel("id-2"))
        result.add(VehicleModel("id-3"))
        result.add(VehicleModel("id-4"))
        result.add(VehicleModel("id-5"))
        result.add(VehicleModel("id-6"))
        result.add(VehicleModel("id-7"))
        result.add(VehicleModel("id-8"))
        result.add(VehicleModel("id-9"))
        return result
    }

    suspend fun getVehicleDetails(vehicleId: String): VehicleDetailsModel {
        val randomCoefficient = if (Random.nextBoolean()) 0.001 else -0.001
        val randomShift = randomCoefficient * Random.nextInt(0..10)
        val newLocationModel = LocationModel(
            TARGET_LATITUDE + randomShift, TARGET_LONGITUDE + randomShift
        )
        delay(Random.nextInt(10..10_000).toLong())
        return VehicleDetailsModel(vehicleId, newLocationModel)
    }
}