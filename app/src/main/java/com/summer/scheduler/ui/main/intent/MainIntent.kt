package com.summer.scheduler.ui.main.intent

sealed class MainIntent {
    object FetchTodos: MainIntent()
    object FetchReminders :MainIntent()
    object AddToDo: MainIntent()
    object AddReminder: MainIntent()
    object SwitchBetweenReminderToDo: MainIntent()
    object OpenBottomSheet : MainIntent()
    object SelectDateFromCalendar : MainIntent()
    object SelectDateFromPicker : MainIntent()
}