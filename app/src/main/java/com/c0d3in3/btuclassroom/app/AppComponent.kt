package com.c0d3in3.btuclassroom.app

import com.c0d3in3.btuclassroom.ui.courses.di.CoursesModule
import com.c0d3in3.btuclassroom.ui.dashboard.di.DashboardModule
import com.c0d3in3.btuclassroom.ui.login.di.LoginModule
import com.c0d3in3.btuclassroom.ui.mail_detail.di.MailDetailModule
import com.c0d3in3.btuclassroom.ui.mail.di.MailModule
import com.c0d3in3.btuclassroom.ui.schedule.di.ScheduleModule
import com.c0d3in3.btuclassroom.ui.courses.CoursesFragment
import com.c0d3in3.btuclassroom.ui.dashboard.DashboardFragment
import com.c0d3in3.btuclassroom.ui.login.LoginFragment
import com.c0d3in3.btuclassroom.ui.mail.MailsFragment
import com.c0d3in3.btuclassroom.ui.mail_detail.MailDetailFragment
import com.c0d3in3.btuclassroom.ui.schedule.ScheduleFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ScheduleModule::class,
        MailModule::class,
        CoursesModule::class,
        DashboardModule::class,
        LoginModule::class,
        MailDetailModule::class]
)
interface AppComponent {

    fun inject(target: LoginFragment)

    fun inject(target: DashboardFragment)

    fun inject(target: CoursesFragment)

    fun inject(target: MailsFragment)

    fun inject(target: MailDetailFragment)

    fun inject(target: ScheduleFragment)
}