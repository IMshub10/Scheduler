package com.summer.scheduler.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summer.scheduler.data.model.repository.ReminderRepository
import com.summer.scheduler.data.model.repository.ToDoRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(ReminderRepository(application), ToDoRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}