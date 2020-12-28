package com.c0d3in3.btuclassroom.ui.courses.di

import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.ui.courses.CoursesViewModel
import dagger.Module

@Module
class CoursesModule {

    fun provideVm(userRepository: UserRepository) = CoursesViewModel(userRepository)
}