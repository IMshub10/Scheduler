package com.summer.scheduler.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.data.model.repository.ReminderRepository
import com.summer.scheduler.data.model.repository.ToDoRepository
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.viewstate.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application,
                    private val reminderRepository: ReminderRepository,
                    private val toDoRepository: ToDoRepository): ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when(it) {
                    is MainIntent.FetchReminders -> fetchAllReminders(it.day)
                    is MainIntent.FetchTodos -> fetchAllToDos(it.day)
                    is MainIntent.SelectDateFromDatePicker -> selectFromDatePicker()
                    is MainIntent.SelectDateFromHorizontalPicker -> selectFromHorizontalPicker()
                    is MainIntent.SwitchBetweenReminderToDo -> switchFragments()
                    is MainIntent.AddToDo -> openToDoFragment()
                    is MainIntent.AddReminder -> openReminderFragment()
                }
            }
        }
    }

    private fun selectFromHorizontalPicker() {
        _state.value = MainState.SelectDateFromHorizontalPicker
    }

    private fun selectFromDatePicker() {
        _state.value = MainState.SelectDateFromDatePicker
    }

    fun setIdleState() {
        _state.value = MainState.Idle
    }

    private fun switchFragments() {
        _state.value = MainState.OpenSwitchBottomSheet
    }

    private fun openReminderFragment() {
        _state.value = MainState.OpenReminderBottomSheet
    }

    private fun openToDoFragment() {
        _state.value = MainState.OpenToDoBottomSheet
    }

    private fun fetchAllReminders(day: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                reminderRepository.getAllReminders(day).collect {
                    _state.value = MainState.Reminders(it)
                    userIntent.send(MainIntent.FetchTodos(day))
                }
            }
        }
        _state.value = MainState.Idle
    }

    private fun fetchAllToDos(day: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                toDoRepository.getAllToDos(day).collect {
                    _state.value = MainState.ToDos(it)
                }
            }
        }
        _state.value = MainState.Idle
    }

    fun addToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.addToDo(toDo)
        }
    }

    fun addReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.addReminder(event)
        }
    }

    fun updateToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.updateToDo(toDo)
        }
    }

    fun updateReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.updateReminder(event)
        }
    }

    fun removeToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.removeToDo(toDo)
        }
    }

    fun removeReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.removeReminder(event)
        }
    }

    fun removeAllToDos(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.removeAllToDos(day)
        }
    }

    fun removeAllReminders(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.removeAllReminders(day)
        }
    }

    fun showDeleteReminderDialog() {
        val dialog = SweetAlertDialog(application.baseContext, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("This Reminder item will be permanently deleted")
            .setConfirmText("Yes")
            .setCancelText("No")
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.setTitleText("Deleted!")
                    .setContentText("Your Reminder item is successfully deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
            }
            .setCancelClickListener {
                it.cancel()
            }

        dialog.show()
    }

    fun showDeleteToDoDialog() {
        val dialog = SweetAlertDialog(application.baseContext, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("This To Do item will be permanently deleted")
            .setConfirmText("Yes")
            .setCancelText("No")
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.setTitleText("Deleted!")
                    .setContentText("Your To Do item is successfully deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
            }
            .setCancelClickListener {
                it.cancel()
            }

        dialog.show()
    }
}