package com.igor_shaula.api_polling.data_layer.data_sources.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RepositoryScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkDSScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FakeDSScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitScope
