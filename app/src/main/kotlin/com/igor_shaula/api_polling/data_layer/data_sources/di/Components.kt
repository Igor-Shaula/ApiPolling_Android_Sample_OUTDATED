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
    dependencies = [FakeApiComponent::class, RetrofitComponent::class],
    modules = [RepositoryModule::class /*FakeApiModule::class, RetrofitModule::class, ContextModule::class*/]
)
@RepositoryScope
interface RepositoryComponent {

    fun inject(sharedViewModel: SharedViewModel)
}

//@Component(dependencies = [RepositoryComponent::class])
//@FakeDSScope
//interface FakeDSComponent

@Component(/*dependencies = [RepositoryComponent::class], */modules = [FakeApiModule::class])
@FakeApiScope
interface FakeApiComponent {
    fun getFakeVehicleGenerator(): FakeVehicleGenerator
}

//@Component(dependencies = [RepositoryComponent::class])
//@NetworkDSScope
//interface NetworkDSComponent

@Component(/*dependencies = [RepositoryComponent::class], */modules = [RetrofitModule::class])
@RetrofitScope
interface RetrofitComponent {
    fun getVehiclesRetrofitNetworkService(): VehiclesRetrofitNetworkService
}
