package com.igor_shaula.api_polling.data.network

import com.igor_shaula.api_polling.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface VehiclesNetworkService {

    @Headers("$API_KEY_HEADER: ${BuildConfig.API_KEY}")
    @GET(API_ENDPOINT_VEHICLES_LIST)
    suspend fun getVehiclesList(): Response<List<VehicleModel>>

    @Headers("$API_KEY_HEADER: ${BuildConfig.API_KEY}")
    @GET("$API_ENDPOINT_VEHICLE_DETAILS_BASE/{$API_PATH_VEHICLE_ID}")
    suspend fun getVehicleDetails(
        @Path(API_PATH_VEHICLE_ID) vehicleId: String
    ): Response<VehicleDetailsModel>
}
