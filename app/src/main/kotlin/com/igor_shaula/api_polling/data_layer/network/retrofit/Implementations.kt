package com.igor_shaula.api_polling.data_layer.network.retrofit

import com.igor_shaula.api_polling.data_layer.network.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network.VehicleModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleNetworkServiceImpl : VehiclesNetworkService {

    override suspend fun getVehiclesList(): Response<List<VehicleModel>> =
        prepareVehiclesNetworkService()
            .getVehiclesList()

    override suspend fun getVehicleDetails(vehicleId: String): Response<VehicleDetailsModel> =
        prepareVehiclesNetworkService()
            .getVehicleDetails(vehicleId)

    private fun prepareVehiclesNetworkService() =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiclesNetworkService::class.java)
}