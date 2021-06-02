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

    @Query("DELETE FROM to_do_table WHERE day == :selectedDay")
    suspend fun deleteAllToDos(selectedDay: Int)

    @Query("SELECT * FROM to_do_table WHERE day == :selectedDay ORDER BY `key` DESC")
    fun getAllToDos(selectedDay: Int): Flow<List<ToDoEntity>>
}