package com.igor_shaula.api_polling.data_layer.stub_source

import com.igor_shaula.api_polling.data_layer.TARGET_LATITUDE
import com.igor_shaula.api_polling.data_layer.TARGET_LONGITUDE
import com.igor_shaula.api_polling.data_layer.network.LocationModel
import com.igor_shaula.api_polling.data_layer.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network.VehicleModel
import kotlinx.coroutines.delay

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
        lateinit var locationModel: LocationModel
        val vehicleRandomIndex = 7
        when (vehicleRandomIndex) {
            7 -> locationModel = LocationModel(TARGET_LATITUDE + 0.0001, TARGET_LONGITUDE + 0.0001)
        }
        delay(100)
        return VehicleDetailsModel(vehicleId, locationModel)
    }
}