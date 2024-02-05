package com.igor_shaula.api_polling.data_layer.data_sources.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RepositoryQualifier(val name: String) // decided to have no default value here
