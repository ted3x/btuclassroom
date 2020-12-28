package com.c0d3in3.btuclassroom.app

import android.app.Application
import android.content.Context
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideSharedPreferences() : SharedPreferencesHandler = SharedPreferencesHandler(app)

    @Provides
    @Singleton
    fun provideResourceProvider() : ResourceProvider = ResourceProvider(app)
}