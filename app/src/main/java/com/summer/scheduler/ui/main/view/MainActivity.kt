package com.summer.scheduler.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodiebag.horizontalpicker.HorizontalPicker
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter
import com.summer.scheduler.ui.main.adapter.ToDoListAdapter
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.AddOption
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.NewEvent
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.NewToDo
import com.summer.scheduler.ui.main.view.calendardialog.DatePickerDialog
import com.summer.scheduler.ui.main.view.calendardialog.DatePickerDialogBoxListener
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import com.summer.scheduler.ui.main.viewstate.MainState
import com.summer.scheduler.utils.SwipeItemTouchHelper
import com.summer.scheduler.utils.listeners.Swipe
import kotlinx.android.synthetic.main.schedule_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),
    NewEvent.OnEventAddedListener,
    NewToDo.OnToDoAddedListener,
    DatePickerDialogBoxListener,
    Swipe{

    private lateinit var mainViewModel: MainViewModel
    private lateinit var reminderAdapter: ReminderListAdapter
    private lateinit var toDoAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupViewModel()
        observableViewModel()

        to_do_newItem.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.AddToDo)
            }
        }

        today_newEvent.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.AddReminder)
            }
        }
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
                        is MainState.OpenToDoBottomSheet -> {
                            openToDoFragment()
                        }
                        is MainState.OpenReminderBottomSheet -> {
                            openReminderFragment()
                        }
                        is MainState.SelectDateFromDatePicker -> {
                            openDatePickerDialog()
                        }
                        is MainState.SelectDateFromHorizontalPicker -> {

                        }
                        is MainState.OpenSwitchBottomSheet -> {
                            openSwitchFragment()
                        }
                    }
                }

            }
        }
    }

    private fun openDatePickerDialog() {
        supportFragmentManager.let {
            DatePickerDialog.newInstance(Bundle()).apply {
                show(it, tag)
            }
        }
    }

    private fun openToDoFragment() {
        supportFragmentManager.let {
            NewToDo.newInstance(Bundle()).apply {
                show(it, tag)
            }
        }
    }

    private fun openReminderFragment() {
        supportFragmentManager.let {
            NewEvent.newInstance(Bundle()).apply {
                show(it, tag)
            }
        }
    }

    private fun openSwitchFragment() {
        supportFragmentManager.let {
            AddOption.newInstance(Bundle()).apply {
                show(it, tag)
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

        setupRecyclerViews()

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

    private fun setupRecyclerViews() {
        to_do_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        today_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        to_do_recyclerView.adapter = toDoAdapter
        today_recyclerView.adapter = reminderAdapter

        val reminderSwipeHelperCallback: ItemTouchHelper.Callback = SwipeItemTouchHelper(
            this,
            0,
            ItemTouchHelper.RIGHT,
            this
        )
        val reminderItemTouchHelper = ItemTouchHelper(reminderSwipeHelperCallback)
        reminderItemTouchHelper.attachToRecyclerView(today_recyclerView)

        val toDoSwipeHelperCallback: ItemTouchHelper.Callback = SwipeItemTouchHelper(
            this,
            0,
            ItemTouchHelper.RIGHT,
            this
        )
        val toDoItemTouchHelper = ItemTouchHelper(toDoSwipeHelperCallback)
        toDoItemTouchHelper.attachToRecyclerView(to_do_recyclerView)
    }

    override fun onEventAdded(event: ReminderEntity) {
        mainViewModel.addReminder(event)
    }

    override fun onToDoAdded(toDo: ToDoEntity) {
        mainViewModel.addToDo(toDo)
    }

    override fun sendDateInfo(dateString: String?, weekNo: Int, date: Date?) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.FetchTodos(dateString!!.toInt()))
            mainViewModel.userIntent.send(MainIntent.FetchReminders(dateString.toInt()))
        }
    }

    override fun rightSwipeDelete(position: Int, recyclerId: Int) {
        if (recyclerId == R.id.today_recyclerView) {
            val reminder = reminderAdapter.currentList[position]
            mainViewModel.removeReminder(reminder)
        } else if (recyclerId == R.id.to_do_recyclerView) {
            val toDo = toDoAdapter.currentList[position]
            mainViewModel.removeToDo(toDo)
        }
    }
}