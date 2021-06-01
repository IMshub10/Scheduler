package com.summer.scheduler.ui.main.viewstate

import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import java.util.*

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    object OpenToDoBottomSheet : MainState()
    object OpenReminderBottomSheet: MainState()
    object OpenSwitchBottomSheet: MainState()
    data class ToDos(val toDos: List<ToDoEntity>) : MainState()
    data class Reminders(val reminders: List<ReminderEntity>) : MainState()
    data class SelectDateFromCalendar(val dateString: String, val weekNo: Int, val date: Date) : MainState()
    data class SelectDateFromPicker(val dateString: String, val weekNo: Int, val date: Date) : MainState()
}