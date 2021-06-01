package com.summer.scheduler.ui.main.intent

import java.util.*

sealed class MainIntent {
    data class FetchTodos(val day: Int): MainIntent()
    data class FetchReminders(val day: Int): MainIntent()
    data class SelectDateFromHorizontalPicker(val dateString: String, val weekNo: Int, val date: Date): MainIntent()
    data class SelectDateFromDatePicker(val dateString: String, val weekNo: Int, val date: Date): MainIntent()
    object AddToDo: MainIntent()
    object AddReminder: MainIntent()
    object SwitchBetweenReminderToDo: MainIntent()
}