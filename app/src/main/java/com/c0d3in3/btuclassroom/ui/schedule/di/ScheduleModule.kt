package com.c0d3in3.btuclassroom.ui.schedule.di

import com.c0d3in3.btuclassroom.ui.schedule.adapter.ScheduleAdapter
import dagger.Module
import dagger.Provides

@Module
class ScheduleModule {

    @Provides
    fun provideAdapter() = ScheduleAdapter()
}