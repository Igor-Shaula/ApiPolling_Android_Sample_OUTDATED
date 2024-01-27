package com.igor_shaula.api_polling.data_layer.data_sources.retrofit

import com.igor_shaula.api_polling.data_layer.data_sources.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.data_sources.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.data_sources.VehicleModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class VehicleRetrofitNetworkServiceImpl {

    private val retrofitNetworkService: VehiclesRetrofitNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiclesRetrofitNetworkService::class.java)
    }

    suspend fun getVehiclesList(): List<VehicleModel> {
        val response = retrofitNetworkService.getVehiclesList()
        if (!response.isSuccessful) {
            Timber.w("getVehiclesList: errorCode = ${response.code()}")
            Timber.w("getVehiclesList: errorBody = ${response.errorBody()?.string()}")
        }
        val result: MutableList<VehicleModel> = mutableListOf()
        response.body()?.let { result.addAll(it) }
        return result
    }

    suspend fun getVehiclesListResult(): Result<List<VehicleModel>> {
        val response = retrofitNetworkService.getVehiclesList()
        val result: Result<List<VehicleModel>> = if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody == null) {
                Result.failure(
                    NetworkGeneralFailure(EMPTY_RESPONSE_BODY_CODE, EMPTY_RESPONSE_BODY_MESSAGE)
                )
            } else {
                val listOfData: MutableList<VehicleModel> = mutableListOf()
                listOfData.addAll(responseBody)
                Result.success(listOfData)
            }
        } else {
            val errorCode = response.code()
            val errorBody = response.errorBody()?.string()
            Timber.i("getVehiclesList: errorCode = $errorCode")
            Timber.i("getVehiclesList: errorBody = $errorBody")
            Result.failure(NetworkGeneralFailure(errorCode, errorBody))
        }
        return result
    }

    suspend fun getVehicleDetails(vehicleId: String): VehicleDetailsModel? =
        retrofitNetworkService.getVehicleDetails(vehicleId).body()

    suspend fun getVehicleDetailsResult(vehicleId: String): Result<VehicleDetailsModel> {
        val response = retrofitNetworkService.getVehicleDetails(vehicleId)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody == null) {
                Result.failure(
                    NetworkGeneralFailure(EMPTY_RESPONSE_BODY_CODE, EMPTY_RESPONSE_BODY_MESSAGE)
                )
            } else {
                Result.success(responseBody)
            }
        } else {
            val errorCode = response.code()
            val errorBody = response.errorBody()?.string() + " for vehicleId: $vehicleId"
            Timber.v("getVehicleDetails: errorCode = $errorCode")
            Timber.v("getVehicleDetails: errorBody = $errorBody")
            Result.failure(NetworkGeneralFailure(errorCode, errorBody))
        }
    }
}

private const val EMPTY_RESPONSE_BODY_CODE = -1
private const val EMPTY_RESPONSE_BODY_MESSAGE = "response.body() is null"

data class NetworkGeneralFailure(val errorCode: Int, val errorMessage: String?) : Throwable()