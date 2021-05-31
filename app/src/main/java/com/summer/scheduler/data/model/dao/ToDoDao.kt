package com.summer.scheduler.data.model.dao

import androidx.room.*
import com.summer.scheduler.data.model.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert
    suspend fun addToDo(toDo: ToDoEntity)

    @Update
    suspend fun updateToDo(toDo: ToDoEntity)

    @Delete
    suspend fun deleteToDo(toDo: ToDoEntity)

    @Query("DELETE FROM reminder_table")
    suspend fun deleteAllToDos()

    @Query("SELECT * FROM to_do_table ORDER BY `key` DESC")
    suspend fun getAllToDos(): Flow<ToDoEntity>
}