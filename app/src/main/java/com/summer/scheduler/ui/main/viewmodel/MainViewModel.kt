package com.summer.scheduler.ui.main.viewmodel

import android.app.Application
import android.util.Log
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

class MainViewModel(private val application: Application, private val reminderRepository: ReminderRepository, private val toDoRepository: ToDoRepository): ViewModel() {

    private val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
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
                    is MainIntent.FetchReminders -> fetchAllReminders()
                    is MainIntent.FetchTodos -> fetchAllToDos()
                    is MainIntent.SwitchBetweenReminderToDo -> switchBetweenToDos()
                    is MainIntent.AddToDo -> openToDoFragment()
                    is MainIntent.AddReminder -> openReminderFragment()
                    else -> Log.d("USER INTENT", "else case")
                }
            }
        }
    }

    private fun switchBetweenToDos() {
        TODO("Not yet implemented")
    }

    private fun openReminderFragment() {

    }

    private fun openToDoFragment() {

    }

    private fun fetchAllReminders() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                reminderRepository.getAllReminders().collect {
                    _state.value = MainState.Reminders(it)
                }
            }
        }
    }

    private fun fetchAllToDos() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                toDoRepository.getAllToDos().collect {
                    _state.value = MainState.ToDos(it)
                }
            }

        }
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

    fun removeAllToDos() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.removeAllToDos()
        }
    }

    fun removeAllReminders() {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.removeAllReminders()
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