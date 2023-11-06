package com.igor_shaula.hometask_zf.network

import com.igor_shaula.hometask_zf.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CarDataNetworksService {
    @Headers("$API_KEY_HEADER: ${BuildConfig.API_KEY}")
    @GET(API_ENDPOINT_VEHICLES)
    suspend fun getCarList(): Response<List<CarModel>>
}
