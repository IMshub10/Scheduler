package com.summer.scheduler.data.model.repository

import com.summer.scheduler.data.model.dao.ToDoDao
import com.summer.scheduler.data.model.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val todoDao: ToDoDao) {

    suspend fun getAllToDos() : Flow<ToDoEntity> = todoDao.getAllToDos()

    suspend fun addToDo(toDo: ToDoEntity) = todoDao.addToDo(toDo)

    suspend fun updateToDo(toDo: ToDoEntity) = todoDao.updateToDo(toDo)

    suspend fun removeToDo(toDo: ToDoEntity) = todoDao.deleteToDo(toDo)

    suspend fun removeAllToDos(toDo: ToDoEntity) = todoDao.deleteAllToDos()
}