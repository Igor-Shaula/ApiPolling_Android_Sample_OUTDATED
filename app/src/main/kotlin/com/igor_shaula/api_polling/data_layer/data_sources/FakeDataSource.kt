package com.igor_shaula.api_polling.data_layer.data_sources

import com.igor_shaula.api_polling.data_layer.CurrentLocation
import com.igor_shaula.api_polling.data_layer.TARGET_LATITUDE
import com.igor_shaula.api_polling.data_layer.TARGET_LONGITUDE
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.data_sources.fake_api_client.FakeVehicleGenerator
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class FakeDataSource @Inject constructor() {

    suspend fun readVehiclesList(): List<VehicleRecord> {
        delay(Random.nextInt(50..5_000).toLong())
        val generator = FakeVehicleGenerator("fake vehicle #")
        val result: MutableList<VehicleModel> = mutableListOf()
        (0..Random.nextInt(30)).forEach { _ ->
            result.add(VehicleModel(generator.createNextVehicleModelString()))
        }
        return result.toVehicleItemRecords()
    }

    suspend fun readVehicleDetails(vehicleId: String): CurrentLocation {
        val randomCoefficient = if (Random.nextBoolean()) 0.001 else -0.001
        val randomShift = randomCoefficient * Random.nextInt(0..10)
        val newLocationModel = LocationModel(
            TARGET_LATITUDE + randomShift, TARGET_LONGITUDE + randomShift
        )
        delay(Random.nextInt(100..3_000).toLong())
        return VehicleDetailsModel(vehicleId, newLocationModel).toVehicleItemRecords()
    }
}
