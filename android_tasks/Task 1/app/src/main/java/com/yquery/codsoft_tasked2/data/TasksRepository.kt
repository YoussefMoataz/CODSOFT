package com.yquery.codsoft_tasked2.data

import androidx.lifecycle.LiveData
import com.yquery.codsoft_tasked2.models.Task
import kotlinx.coroutines.flow.Flow

class TasksRepository(private val tasksDao: TasksDAO) {

    val getAllTasks: LiveData<MutableList<Task>> = tasksDao.getAllTasks()

    fun getTask(id: Int): Flow<Task> {
        return tasksDao.getTask(id)
    }

    suspend fun insertTask(task: Task) {
        tasksDao.insertTask(task)
    }

    suspend fun deleteAllTasks() {
        tasksDao.deleteAllTasks()
    }

    suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }

    suspend fun deleteTaskByID(id: Int) {
        tasksDao.deleteTaskByID(id)
    }

    suspend fun updateTask(task: Task) {
        tasksDao.updateTask(task)
    }

}