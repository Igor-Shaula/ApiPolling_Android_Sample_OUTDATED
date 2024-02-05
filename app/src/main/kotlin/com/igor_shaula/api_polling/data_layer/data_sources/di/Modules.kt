package com.igor_shaula.api_polling.data_layer.data_sources.di

import android.content.Context
import com.igor_shaula.api_polling.ThisApp
import com.igor_shaula.api_polling.data_layer.DefaultVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehiclesRepository
import com.igor_shaula.api_polling.data_layer.data_sources.API_BASE_URL
import com.igor_shaula.api_polling.data_layer.data_sources.FakeDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.NetworkDataSource
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehicleRetrofitNetworkServiceImpl
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehiclesRetrofitNetworkService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val REPOSITORY_TYPE_NETWORK = "using data from NETWORK source"
const val REPOSITORY_TYPE_FAKE = "using data from FAKE source"

@Module
class RepositoryModule {

//    @[Provides RepositoryScope]
//    fun provideActiveDataStore(): ThisApp.ActiveDataSource = ThisApp.ActiveDataSource.FAKE

//    @[Provides RepositoryScope]
//    fun providePollingEngine(): PollingEngine =
//        JavaTPEBasedPollingEngine(5)

    @[Provides RepositoryScope RepositoryQualifier(REPOSITORY_TYPE_FAKE)]
    fun provideFakeRepository(): VehiclesRepository {
        val vrns: VehiclesRetrofitNetworkService =
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VehiclesRetrofitNetworkService::class.java)
        return DefaultVehiclesRepository(
            NetworkDataSource(VehicleRetrofitNetworkServiceImpl(vrns)),
            FakeDataSource(),
            ThisApp.ActiveDataSource.FAKE
        )
    }

    @[Provides RepositoryScope RepositoryQualifier(REPOSITORY_TYPE_NETWORK)]
    fun provideNetworkRepository(): VehiclesRepository {
        val vrns: VehiclesRetrofitNetworkService =
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VehiclesRetrofitNetworkService::class.java)
        return DefaultVehiclesRepository(
            NetworkDataSource(VehicleRetrofitNetworkServiceImpl(vrns)),
            FakeDataSource(),
            ThisApp.ActiveDataSource.NETWORK
        )
    }
}

@Module
class RetrofitModule { // why in fact this is not used ???

    @[Provides RepositoryScope] // replace RepositoryScope with RetrofitScope later
    fun provideRetrofitService(): VehiclesRetrofitNetworkService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehiclesRetrofitNetworkService::class.java)
}

@Module
class ContextModule(private val appContext: Context) {

    @[Provides Singleton] // because application context is used in fact
    fun provideContext(): Context = appContext
}
