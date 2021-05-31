package com.summer.scheduler.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.viewstate.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel :ViewModel() {
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
            }
        }
    }
}