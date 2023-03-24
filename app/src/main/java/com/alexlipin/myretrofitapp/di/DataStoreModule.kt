package com.alexlipin.myretrofitapp.di

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.alexlipin.myretrofitapp.repository.dao.NumberDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideNumberDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
                app,
                NumberDatabase::class.java,
                "numbers.db"
            ).build()

    @Provides
    @Singleton
    fun provideNumberDao(db: NumberDatabase) = db.dao()
}