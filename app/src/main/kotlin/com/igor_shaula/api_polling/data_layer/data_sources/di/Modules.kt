package com.igor_shaula.api_polling.data_layer.data_sources.di

import com.igor_shaula.api_polling.data_layer.data_sources.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehiclesRetrofitNetworkService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule {

    @Provides
    @RetrofitScope
    fun provideRetrofitService(): VehiclesRetrofitNetworkService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiclesRetrofitNetworkService::class.java)
}
