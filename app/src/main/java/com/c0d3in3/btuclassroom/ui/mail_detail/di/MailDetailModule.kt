package com.c0d3in3.btuclassroom.ui.mail_detail.di

import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.di.NetworkModule
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import com.c0d3in3.btuclassroom.ui.mail_detail.MailDetailViewModel
import dagger.Module

@Module(
    includes = [NetworkModule::class]
)
class MailDetailModule {

    fun provideVm(resourceProvider: ResourceProvider, networkHandler: NetworkHandler) =
        MailDetailViewModel(resourceProvider, networkHandler)
}