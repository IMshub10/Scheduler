package com.summer.scheduler.ui.main.viewstate

import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    object OpenToDoBottomSheet : MainState()
    object OpenReminderBottomSheet: MainState()
    object OpenSwitchBottomSheet: MainState()
    data class ToDos(val toDos: List<ToDoEntity>) : MainState()
    data class Reminders(val reminders: List<ReminderEntity>) : MainState()
    object SelectDateFromDatePicker : MainState()
    data class SelectDateFromHorizontalPicker(val day:Int) : MainState()
}