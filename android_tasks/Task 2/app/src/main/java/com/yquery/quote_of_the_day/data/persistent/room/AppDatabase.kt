package com.yquery.quote_of_the_day.data.persistent.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yquery.quote_of_the_day.data.domain.Quote
import com.yquery.quote_of_the_day.data.persistent.QuotesDao

@Database(entities = [Quote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quotesDao(): QuotesDao

}