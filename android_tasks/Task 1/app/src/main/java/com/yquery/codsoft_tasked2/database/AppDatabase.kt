package com.yquery.codsoft_tasked2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yquery.codsoft_tasked2.data.TasksDAO
import com.yquery.codsoft_tasked2.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TaskedTwoDatabaseYMoataz"
                )
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

}