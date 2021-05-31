package com.summer.scheduler.data.model.repository

import com.summer.scheduler.data.model.dao.ReminderDao
import com.summer.scheduler.data.model.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow


class ReminderRepository(private val reminderDao: ReminderDao) {
    suspend fun getAllReminders() : Flow<ReminderEntity> = reminderDao.getAllReminders()

    suspend fun addReminder(reminder: ReminderEntity) = reminderDao.addReminder(reminder)

    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)

    suspend fun removeReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)

    suspend fun removeAllReminders(reminder: ReminderEntity) = reminderDao.deleteAllReminders()
}