package com.yquery.tasked2.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yquery.tasked2.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDAO {

    @Query("SELECT * FROM table_tasks ORDER BY taskID DESC")
    fun getAllTasks(): LiveData<MutableList<Task>>

    @Query("SELECT * FROM table_tasks WHERE taskID = :id")
    fun getTask(id: Int): Flow<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Query("DELETE FROM table_tasks")
    suspend fun deleteAllTasks()

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM table_tasks WHERE taskID = :id")
    suspend fun deleteTaskByID(id: Int)

    @Update
    suspend fun updateTask(task: Task)

}