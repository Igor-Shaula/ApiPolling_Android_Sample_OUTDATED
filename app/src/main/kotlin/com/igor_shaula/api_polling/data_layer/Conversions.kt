package com.igor_shaula.api_polling.data_layer

fun List<Pair<String, VehicleRecord>>.toVehicleRecordList(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this.forEach {
        result.add(it.second)
    }
    return result
}
