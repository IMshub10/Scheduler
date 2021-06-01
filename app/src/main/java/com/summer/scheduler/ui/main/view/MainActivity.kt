package com.summer.scheduler.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodiebag.horizontalpicker.HorizontalPicker
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter
import com.summer.scheduler.ui.main.adapter.ToDoListAdapter
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import com.summer.scheduler.ui.main.viewstate.MainState
import kotlinx.android.synthetic.main.schedule_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect {
                    when (it) {
                        is MainState.Idle -> {
                        }
                        is MainState.Loading -> {
                        }
                        is MainState.Reminders -> {
                            getAllReminders(it.reminders)
                        }
                        is MainState.ToDos -> {
                            getAllToDos(it.toDos)
                        }
                        is MainState.OpenBottomSheet -> {
                        }
                        is MainState.SelectDateFromCalendar -> {
                        }
                        is MainState.SelectDateFromPicker -> {
                        }
                    }
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
        toDoAdapter = ToDoListAdapter(this)

        to_do_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        today_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        to_do_recyclerView.adapter = toDoAdapter
        today_recyclerView.adapter = reminderAdapter

        val hpText: HorizontalPicker = findViewById(R.id.hpicker)

        val textItems: MutableList<HorizontalPicker.PickerItem> =
            ArrayList()
        for (i in 1..4) {
            textItems.add(HorizontalPicker.TextItem("S$i"))
        }
        hpText.setItems(
            textItems,
            3
        ) //3 here signifies the default selected item. Use : hpText.setItems(textItems) if none of the items are selected by default.

    }
}