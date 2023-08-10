package com.yquery.tasked2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yquery.tasked2.data.TasksRepository
import com.yquery.tasked2.database.AppDatabase
import com.yquery.tasked2.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    val getAllTasks: LiveData<MutableList<Task>>

    private val tasksRepository: TasksRepository

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        tasksRepository = TasksRepository(taskDao)

        getAllTasks = tasksRepository.getAllTasks

    }

    suspend fun getTask(id: Int): Task {
        return tasksRepository.getTask(id).first()
    }

    fun insertTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.insertTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.deleteAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.deleteTask(task)
        }
    }

    fun deleteTaskByID(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.deleteTaskByID(id)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.updateTask(task)
        }
    }

}