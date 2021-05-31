package com.summer.scheduler.data.model.repository

import android.app.Application
import com.summer.scheduler.data.model.dao.ReminderDao
import com.summer.scheduler.data.model.database.ReminderDatabase
import com.summer.scheduler.data.model.entity.ReminderEntity


class ReminderRepository(application: Application) {

    private val reminderDao: ReminderDao

    init {
        val database = ReminderDatabase.getDatabase(application)
        reminderDao = database.getReminderDao()
    }


    suspend fun getAllReminders() : ArrayList<ReminderEntity> = reminderDao.getAllReminders()

    suspend fun addReminder(reminder: ReminderEntity) = reminderDao.addReminder(reminder)

    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)

    suspend fun removeReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)

    suspend fun removeAllReminders(reminder: ReminderEntity) = reminderDao.deleteAllReminders()
}