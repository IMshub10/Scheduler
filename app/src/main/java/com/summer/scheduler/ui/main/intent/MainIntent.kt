package com.summer.scheduler.ui.main.intent

import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity

sealed class MainIntent {
    data class FetchTodos(val day: Int): MainIntent()
    data class FetchReminders(val day: Int): MainIntent()
    data class SelectDateFromHorizontalPicker(val date: Int): MainIntent()
    data class UpdateToDo(val toDo: ToDoEntity): MainIntent()
    data class UpdateReminder(val reminder: ReminderEntity): MainIntent()
    object SelectDateFromDatePicker: MainIntent()
    object AddToDo: MainIntent()
    object AddReminder: MainIntent()
    object SwitchBetweenReminderToDo: MainIntent()
}