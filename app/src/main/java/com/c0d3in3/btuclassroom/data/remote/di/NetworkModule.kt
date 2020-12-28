package com.c0d3in3.btuclassroom.data.remote.di

import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun networkHandler(resourceProvider: ResourceProvider) = NetworkHandler(resourceProvider)

}