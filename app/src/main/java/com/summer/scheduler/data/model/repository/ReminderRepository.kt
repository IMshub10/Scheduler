package com.summer.scheduler.data.model.repository

import android.app.Application
import com.summer.scheduler.data.model.dao.ReminderDao
import com.summer.scheduler.data.model.database.ReminderDatabase
import com.summer.scheduler.data.model.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow


class ReminderRepository(application: Application) {

    private val reminderDao: ReminderDao

    init {
        val database = ReminderDatabase.getDatabase(application)
        reminderDao = database.getReminderDao()
    }


    fun getAllReminders(day: Int) : Flow<List<ReminderEntity>> = reminderDao.getAllReminders(day)

    suspend fun addReminder(reminder: ReminderEntity) = reminderDao.addReminder(reminder)

    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)

    suspend fun removeReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)

    suspend fun removeAllReminders(day: Int) = reminderDao.deleteAllReminders(day)
}