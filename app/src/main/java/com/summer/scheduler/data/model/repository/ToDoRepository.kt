package com.summer.scheduler.data.model.repository

import android.app.Application
import com.summer.scheduler.data.model.dao.ToDoDao
import com.summer.scheduler.data.model.database.ToDoDatabase
import com.summer.scheduler.data.model.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow

class ToDoRepository(application: Application) {

    private val todoDao: ToDoDao

    init {
        val database = ToDoDatabase.getDatabase(application)
        todoDao = database.getToDoDao()
    }

    fun getAllToDos() : Flow<List<ToDoEntity>> = todoDao.getAllToDos()

    suspend fun addToDo(toDo: ToDoEntity) = todoDao.addToDo(toDo)

    suspend fun updateToDo(toDo: ToDoEntity) = todoDao.updateToDo(toDo)

    suspend fun removeToDo(toDo: ToDoEntity) = todoDao.deleteToDo(toDo)

    suspend fun removeAllToDos() = todoDao.deleteAllToDos()
}