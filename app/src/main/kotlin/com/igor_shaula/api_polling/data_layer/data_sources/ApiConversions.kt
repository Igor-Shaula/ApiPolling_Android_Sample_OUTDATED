package com.igor_shaula.api_polling.data_layer.data_sources

import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.NetworkGeneralFailure

fun List<VehicleModel>?.toVehicleItemRecords(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this?.forEach {
        result.add(VehicleRecord(vehicleId = it.vehicleId, vehicleStatus = VehicleStatus.UNKNOWN))
    }
    return result
}

fun Result<List<VehicleModel>>.toVehicleItemRecordsResult(): Result<List<VehicleRecord>> =
    if (this.isFailure) {
        val networkLevelException = this.exceptionOrNull() as NetworkGeneralFailure?
        val explanation = if (networkLevelException == null) {
            ABSENT_FAILURE_INSTANCE_MESSAGE
        } else {
            "remote API error code: " + networkLevelException.errorCode + ",\n" +
                    "remote API error message:\n" + networkLevelException.errorMessage
        }
        Result.failure(DataLayerGeneralFailure(explanation))
    } else {
        val resultList = mutableListOf<VehicleRecord>()
        this.getOrNull()?.forEach {
            resultList.add(
                VehicleRecord(vehicleId = it.vehicleId, vehicleStatus = VehicleStatus.UNKNOWN)
            )
        }
        // if incoming list was empty (valid case) - a new empty list is emitted - there is no error with it
        Result.success(resultList)
        // here i don't emit Result.failure instance for nullable case - because in fact it cannot happen
    }

fun VehicleDetailsModel.toVehicleItemRecords(): VehicleDetailsRecord =
    VehicleDetailsRecord(
        vehicleId = this.vehicleId,
        latitude = this.location.latitude,
        longitude = this.location.longitude
    )

private const val ABSENT_FAILURE_INSTANCE_MESSAGE = "absent failure instance in Network API Result"

data class DataLayerGeneralFailure(val explanation: String) : Throwable()