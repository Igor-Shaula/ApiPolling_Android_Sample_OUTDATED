package com.igor_shaula.api_polling.data_layer.data_sources.di

import dagger.Component

@Component(dependencies = [FakeDSComponent::class, NetworkDSComponent::class])
@RepositoryScope
interface RepositoryComponent {
    // TODO: add inject() here
}

@Component
@FakeDSScope
interface FakeDSComponent

@Component(dependencies = [RetrofitComponent::class])
@NetworkDSScope
interface NetworkDSComponent

@Component(modules = [RetrofitModule::class])
@RetrofitScope
interface RetrofitComponent
