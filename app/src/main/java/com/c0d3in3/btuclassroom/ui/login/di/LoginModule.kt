package com.c0d3in3.btuclassroom.ui.login.di

import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.di.NetworkModule
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import com.c0d3in3.btuclassroom.ui.login.LoginViewModel
import dagger.Module

@Module(
    includes = [NetworkModule::class]
)
class LoginModule {

    fun provideVm(
        sharedPreferencesHandler: SharedPreferencesHandler,
        userRepository: UserRepository,
        resourceProvider: ResourceProvider,
        networkHandler: NetworkHandler
    ) = LoginViewModel(sharedPreferencesHandler, userRepository, resourceProvider, networkHandler)
}