package com.summer.scheduler.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter
import com.summer.scheduler.ui.main.adapter.ToDoListAdapter
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import com.summer.scheduler.ui.main.viewstate.MainState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var reminderAdapter: ReminderListAdapter
    private lateinit var toDoAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupViewModel()
        observableViewModel()
    }

    private fun observableViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {}
                    is MainState.Loading -> {}
                    is MainState.Reminders -> {
                        getAllReminders(it.reminders)
                    }
                    is MainState.ToDos -> {
                        getAllToDos(it.toDos)
                    }
                    is MainState.OpenBottomSheet -> {}
                    is MainState.SelectDateFromCalendar -> {}
                    is MainState.SelectDateFromPicker -> {}
                }
            }
        }
    }

    private fun getAllReminders(reminders: List<ReminderEntity>) {
        reminderAdapter.submitList(reminders)
    }

    private fun getAllToDos(toDos: List<ToDoEntity>) {
        toDoAdapter.submitList(toDos)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory(application))
            .get(MainViewModel::class.java)
    }

    private fun setupUI() {
        reminderAdapter = ReminderListAdapter(this)
    }
}