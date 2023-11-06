package com.igor_shaula.hometask_zf

class CarsRepository {

    fun readTheData(): List<CarItemRecord> {
        val result = mutableListOf<CarItemRecord>()
        result.add(CarItemRecord("1", CarStatus.CLOSE))
        result.add(CarItemRecord("2", CarStatus.FAR_AWAY))
        result.add(CarItemRecord("3", CarStatus.FAR_AWAY))
        return result
    }
}