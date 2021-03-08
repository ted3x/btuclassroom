package com.c0d3in3.btuclassroom.ui.schedule.di

import com.c0d3in3.btuclassroom.data.local.RoomModule
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.ui.schedule.ScheduleViewModel
import com.c0d3in3.btuclassroom.ui.schedule.adapter.ScheduleAdapter
import dagger.Module
import dagger.Provides

@Module(
    includes = [RoomModule::class]
)
class ScheduleModule {

    @Provides
    fun provideVm(userRepository: UserRepository) = ScheduleViewModel(userRepository)

    @Provides
    fun provideAdapter() = ScheduleAdapter()
}