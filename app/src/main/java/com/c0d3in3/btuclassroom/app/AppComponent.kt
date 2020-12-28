package com.c0d3in3.btuclassroom.app

import com.c0d3in3.btuclassroom.data.local.RoomModule
import com.c0d3in3.btuclassroom.ui.courses.CoursesFragment
import com.c0d3in3.btuclassroom.ui.dashboard.DashboardFragment
import com.c0d3in3.btuclassroom.ui.login.LoginFragment
import com.c0d3in3.btuclassroom.ui.mail.MailsFragment
import com.c0d3in3.btuclassroom.ui.mail_detail.MailDetailFragment
import com.c0d3in3.btuclassroom.ui.schedule.ScheduleFragment
import com.c0d3in3.btuclassroom.ui.schedule.di.ScheduleModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ScheduleModule::class,
        RoomModule::class]
)
interface AppComponent {

    fun inject(target: LoginFragment)

    fun inject(target: DashboardFragment)

    fun inject(target: CoursesFragment)

    fun inject(target: MailsFragment)

    fun inject(target: MailDetailFragment)

    fun inject(target: ScheduleFragment)
}