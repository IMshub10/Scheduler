package com.summer.scheduler.data.model.dao

import androidx.room.*
import com.summer.scheduler.data.model.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun addReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminder_table")
    suspend fun deleteAllReminders()

    @Query("SELECT * FROM reminder_table ORDER BY `start` DESC")
    fun getAllReminders(): Flow<List<ReminderEntity>>
}