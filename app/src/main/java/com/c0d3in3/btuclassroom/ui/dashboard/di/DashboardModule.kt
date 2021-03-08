package com.c0d3in3.btuclassroom.ui.dashboard.di

import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.ui.dashboard.DashboardViewModel
import dagger.Module

@Module
class DashboardModule {

    fun provideVm(userRepository: UserRepository) =  DashboardViewModel(userRepository)
}