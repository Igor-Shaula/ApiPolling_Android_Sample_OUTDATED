package com.igor_shaula.api_polling.data_layer.data_sources.di

import android.content.Context
import com.igor_shaula.api_polling.data_layer.data_sources.fake_api_client.FakeVehicleGenerator
import com.igor_shaula.api_polling.data_layer.data_sources.retrofit.VehiclesRetrofitNetworkService
import com.igor_shaula.api_polling.ui_layer.SharedViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ContextModule::class])
@Singleton
interface ContextComponent {
    fun getContext(): Context
}

@Component(
    dependencies = [FakeDSComponent::class, NetworkDSComponent::class],
//    dependencies = [FakeApiComponent::class, RetrofitComponent::class], // very simplified version
    modules = [RepositoryModule::class]
)
@RepositoryScope
interface RepositoryComponent {

    fun inject(sharedViewModel: SharedViewModel)
}

@Component(dependencies = [FakeApiComponent::class])
@FakeDSScope
interface FakeDSComponent {
    fun getFakeVehicleGenerator(): FakeVehicleGenerator
}

@Component(modules = [FakeApiModule::class])
@FakeApiScope
interface FakeApiComponent {
    fun getFakeVehicleGenerator(): FakeVehicleGenerator
}

@Component(dependencies = [RetrofitComponent::class])
@NetworkDSScope
interface NetworkDSComponent {
    fun getVehiclesRetrofitNetworkService(): VehiclesRetrofitNetworkService
}

@Component(modules = [RetrofitModule::class])
@RetrofitScope
interface RetrofitComponent {
    fun getVehiclesRetrofitNetworkService(): VehiclesRetrofitNetworkService
}
