package com.yquery.tasked2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val taskID: Int,
    @ColumnInfo(name = "title", defaultValue = "Title") var taskTitle: String?,
    @ColumnInfo(name = "details", defaultValue = "Details") var taskDetails: String?,
    @ColumnInfo(name = "date", defaultValue = "Date") var taskDate: String?,
    @ColumnInfo(name = "time", defaultValue = "Time") var taskTime: String?,
    @ColumnInfo(name = "completed", defaultValue = "false") var taskCompleted: Boolean?,
)