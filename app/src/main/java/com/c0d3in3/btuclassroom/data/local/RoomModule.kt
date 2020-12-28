package com.c0d3in3.btuclassroom.data.local

import android.content.Context
import androidx.room.RoomDatabase
import com.c0d3in3.btuclassroom.data.local.AppDatabase
import com.c0d3in3.btuclassroom.data.local.user.UserDao
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Context): AppDatabase {
        return AppDatabase.build(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}