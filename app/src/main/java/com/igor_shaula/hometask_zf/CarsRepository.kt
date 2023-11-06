package com.igor_shaula.hometask_zf

import com.igor_shaula.hometask_zf.network.CarDataNetworksServiceImpl
import com.igor_shaula.hometask_zf.network.CarModel

class CarsRepository {

    suspend fun readTheData(): List<CarItemRecord> {
        val carDataNetworksService = CarDataNetworksServiceImpl()
        val carList = carDataNetworksService.getCarList()
        return carList.body().toCarItemRecords()
    }
}

private fun List<CarModel>?.toCarItemRecords(): List<CarItemRecord> {
    val result = mutableListOf<CarItemRecord>()
    this?.forEach {
        result.add(CarItemRecord(carId = it.vehicleId, carStatus = CarStatus.FAR_AWAY))
    }
    return result
}
