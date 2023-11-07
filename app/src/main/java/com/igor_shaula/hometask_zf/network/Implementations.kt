package com.igor_shaula.hometask_zf.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleNetworkServiceImpl : VehiclesNetworkService {

    override suspend fun getVehiclesList(): Response<List<VehicleModel>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(VehiclesNetworkService::class.java)
        return service.getVehiclesList()
    }
}