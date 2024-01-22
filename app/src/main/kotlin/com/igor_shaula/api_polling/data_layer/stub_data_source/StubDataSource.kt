package com.igor_shaula.api_polling.data_layer.stub_data_source

import com.igor_shaula.api_polling.data_layer.TARGET_LATITUDE
import com.igor_shaula.api_polling.data_layer.TARGET_LONGITUDE
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.network_data_source.LocationModel
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleModel
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords
import com.igor_shaula.stub_data_generator.StubVehicleGenerator
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

class StubDataSource {

    suspend fun readVehiclesList(): List<VehicleRecord> {
        delay(Random.nextInt(50..5_000).toLong())
        val generator = StubVehicleGenerator("stub vehicle #")
        val result: MutableList<VehicleModel> = mutableListOf()
        (0..Random.nextInt(30)).forEach { _ ->
            result.add(VehicleModel(generator.createNextVehicleModelString()))
        }
        return result.toVehicleItemRecords()
    }

    suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord {
        val randomCoefficient = if (Random.nextBoolean()) 0.001 else -0.001
        val randomShift = randomCoefficient * Random.nextInt(0..10)
        val newLocationModel = LocationModel(
            TARGET_LATITUDE + randomShift, TARGET_LONGITUDE + randomShift
        )
        delay(Random.nextInt(100..3_000).toLong())
        return VehicleDetailsModel(vehicleId, newLocationModel).toVehicleItemRecords()
    }
}
