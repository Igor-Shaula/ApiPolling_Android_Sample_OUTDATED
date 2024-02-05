package com.igor_shaula.api_polling.data_layer.data_sources.di

import com.igor_shaula.api_polling.ui_layer.SharedViewModel
import dagger.Component

@Component(
//    dependencies = [FakeDSComponent::class, NetworkDSComponent::class],
    modules = [RepositoryModule::class, RetrofitModule::class]
)
@RepositoryScope
interface RepositoryComponent {

    fun inject(sharedViewModel: SharedViewModel)
}

//@Component(dependencies = [RepositoryComponent::class])
//@FakeDSScope
//interface FakeDSComponent
//
//@Component(dependencies = [RepositoryComponent::class])
//@NetworkDSScope
//interface NetworkDSComponent
//
//@Component(dependencies = [NetworkDSComponent::class], modules = [RetrofitModule::class])
//@RetrofitScope
//interface RetrofitComponent
