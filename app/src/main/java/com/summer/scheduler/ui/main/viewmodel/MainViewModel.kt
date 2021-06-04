package com.summer.scheduler.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.data.model.repository.ReminderRepository
import com.summer.scheduler.data.model.repository.ToDoRepository
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.viewstate.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val reminderRepository: ReminderRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    private var job1: Job? = null
    private var job2: Job? = null

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchReminders -> fetchAllReminders(it.day)
                    is MainIntent.FetchTodos -> fetchAllToDos(it.day)
                    is MainIntent.SelectDateFromDatePicker -> selectFromDatePicker()
                    is MainIntent.SelectDateFromHorizontalPicker -> selectFromHorizontalPicker(it.date)
                    is MainIntent.SwitchBetweenReminderToDo -> switchFragments()
                    is MainIntent.AddToDo -> openToDoFragment()
                    is MainIntent.AddReminder -> openReminderFragment()
                    is MainIntent.UpdateReminder -> updateReminder(it.reminder)
                    is MainIntent.UpdateToDo -> updateToDo(it.toDo)
                }
            }
        }
    }

    private fun selectFromHorizontalPicker(date: Int) {
        _state.value = MainState.SelectDateFromHorizontalPicker(date)
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

    private suspend fun fetchAllReminders(day: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            if (job1 != null) {
                if (job1?.isCompleted == false) {
                    job1?.cancel()
                }
            }
            job1 = viewModelScope.launch(Dispatchers.IO) {
                reminderRepository.getAllReminders(day).collect {
                    Log.e("collecting1", "reminders")
                    _state.value = MainState.Reminders(it)
                    Log.e("collecting2", "reminders")
                    viewModelScope.launch {
                        userIntent.send(MainIntent.FetchTodos(day))
                    }
                }
            }
        }
    }

    private suspend fun fetchAllToDos(day: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            if (job2 != null) {
                if (job2?.isCompleted == false) {
                    job2?.cancel()
                }
            }
            job2 = viewModelScope.launch(Dispatchers.IO) {
                toDoRepository.getAllToDos(day).collect {
                    Log.e("collecting1", "to dos")
                    _state.value = MainState.ToDos(it)
                    Log.e("collecting2", "to dos")
                }
            }
        }
    }

    suspend fun addToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.addToDo(toDo)
        }
    }

    suspend fun addReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.addReminder(event)
        }
    }

    private suspend fun updateToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.updateToDo(toDo)
        }
    }

    private suspend fun updateReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.updateReminder(event)
        }
    }

    suspend fun removeToDo(toDo: ToDoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.removeToDo(toDo)
        }
    }

    suspend fun removeReminder(event: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.removeReminder(event)
        }
    }

    suspend fun removeAllToDos(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.removeAllToDos(day)
        }
    }

    suspend fun removeAllReminders(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.removeAllReminders(day)
        }
    }
}