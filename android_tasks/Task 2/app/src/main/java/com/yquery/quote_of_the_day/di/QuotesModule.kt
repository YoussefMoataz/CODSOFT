package com.yquery.quote_of_the_day.di

import android.content.Context
import androidx.room.Room
import com.yquery.quote_of_the_day.data.persistent.QuotesDao
import com.yquery.quote_of_the_day.data.persistent.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object QuotesModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): QuotesDao {
        return appDatabase.quotesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "QuoteOfTheDayDatabase"
        ).build()
    }

}