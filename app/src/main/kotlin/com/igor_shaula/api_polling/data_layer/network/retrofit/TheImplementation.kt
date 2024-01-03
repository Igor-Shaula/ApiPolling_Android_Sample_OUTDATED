package com.igor_shaula.api_polling.data_layer.network.retrofit

import com.igor_shaula.api_polling.data_layer.network.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network.VehicleModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleRetrofitNetworkServiceImpl {

    private val retrofitNetworkService: VehiclesRetrofitNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiclesRetrofitNetworkService::class.java)
    }

    suspend fun getVehiclesList(): List<VehicleModel>? =
        retrofitNetworkService.getVehiclesList().body()

    suspend fun getVehicleDetails(vehicleId: String): VehicleDetailsModel? =
        retrofitNetworkService.getVehicleDetails(vehicleId).body()
}
