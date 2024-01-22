package com.igor_shaula.api_polling.data_layer.network_data_source.retrofit

import com.igor_shaula.api_polling.data_layer.network_data_source.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network_data_source.VehicleModel
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

    suspend fun getVehicleDetails(vehicleId: String): VehicleDetailsModel? =
        retrofitNetworkService.getVehicleDetails(vehicleId).body()
}
