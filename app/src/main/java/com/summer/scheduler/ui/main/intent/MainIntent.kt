package com.summer.scheduler.ui.main.intent

sealed class MainIntent {
    object FetchTodos: MainIntent()
    object FetchReminders :MainIntent()
    object OpenBottomSheet : MainIntent()
    object SelectDateFromCalendar : MainIntent()
    object SelectDateFromPicker : MainIntent()
}