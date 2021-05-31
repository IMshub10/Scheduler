package com.summer.scheduler.ui.main.viewstate

import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    object OpenBottomSheet : MainState()
    data class ToDos(val toDos: List<ToDoEntity>) : MainState()
    data class Reminders(val reminders: List<ReminderEntity>) : MainState()
    data class SelectDateFromCalendar(val day: Int) : MainState()
    data class SelectDateFromPicker(val day: Int) : MainState()
}