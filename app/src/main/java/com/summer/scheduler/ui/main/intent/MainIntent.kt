package com.summer.scheduler.ui.main.intent

sealed class MainIntent {
    data class FetchTodos(val day: Int): MainIntent()
    data class FetchReminders(val day: Int): MainIntent()
    data class SelectDateFromHorizontalPicker(val date: Int): MainIntent()
    object SelectDateFromDatePicker: MainIntent()
    object AddToDo: MainIntent()
    object AddReminder: MainIntent()
    object SwitchBetweenReminderToDo: MainIntent()
}