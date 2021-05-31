package com.summer.scheduler.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MainViewModel(private val reminderRepository: ReminderRepository, private val toDoRepository: ToDoRepository): ViewModel() {

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
                    else -> Log.d("USER INTENT", "else case")
                }
            }
        }
    }

    private fun fetchAllReminders() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                reminderRepository.getAllReminders().collect {
                    MainState.Reminders(it)
                }
            }
        }
    }

    private fun fetchAllToDos() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                toDoRepository.getAllToDos().collect {
                    MainState.ToDos(it)
                }
            }

        }
    }
}