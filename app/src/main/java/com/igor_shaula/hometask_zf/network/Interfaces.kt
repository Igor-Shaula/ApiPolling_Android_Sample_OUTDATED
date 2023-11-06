package com.igor_shaula.hometask_zf.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CarDataNetworksService {
    @Headers("$API_KEY_HEADER: secret must not be exposed to the open public")
    @GET(API_ENDPOINT_VEHICLES)
    suspend fun getCarList(): Response<List<CarModel>>
}
