package com.example.chart2.di

import android.app.Application
import androidx.room.Room
import com.example.chart2.database.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(app, MainDatabase::class.java, "main_database").build()

    @Provides
    fun provideDao(db: MainDatabase) = db.dao
}