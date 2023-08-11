package com.yquery.quote_of_the_day.data.persistent.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yquery.quote_of_the_day.data.persistent.QuotesDao
import com.yquery.quote_of_the_day.data.persistent.domain.FavouriteQuote

@Database(entities = [FavouriteQuote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quotesDao(): QuotesDao

}