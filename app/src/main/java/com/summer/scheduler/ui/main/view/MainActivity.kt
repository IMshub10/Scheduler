package com.summer.scheduler.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var weekNo = 0
    private lateinit var mainViewModel: MainViewModel
    private lateinit var reminderAdapter: ReminderListAdapter
    private lateinit var toDoAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupUI()
        startingDay()
        setupViewModel()
        observableViewModel()
        setOnDateClickListeners()
    }

    private fun observableViewModel() {
        lifecycleScope.launch {
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

        to_do_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        today_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        to_do_recyclerView.adapter = toDoAdapter
        today_recyclerView.adapter = reminderAdapter

    }


    private fun returnFirstTwoChars(d: String): String {
        return d.substring(0, 2)
    }

    private fun setDateToCardViews(weekNo: Int) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val cal = Calendar.getInstance()
        cal[Calendar.WEEK_OF_YEAR] = weekNo
        cal.firstDayOfWeek = Calendar.SUNDAY
        cal[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        monDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
        tueDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
        wedDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
        thuDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
        friDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
        satDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
        cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        sunDate!!.text = returnFirstTwoChars(sdf.format(cal.time))
    }

    private fun setColorDaySelected(day: Int) {
        when (day) {
            1 -> {
                colorBlack()
                sunDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            2 -> {
                colorBlack()
                monDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            3 -> {
                colorBlack()
                tueDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            4 -> {
                colorBlack()
                wedDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            5 -> {
                colorBlack()
                thuDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            6 -> {
                colorBlack()
                friDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
            7 -> {
                colorBlack()
                satDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            }
        }
    }

    private fun colorBlack() {
        sunDate!!.setBackgroundResource(R.drawable.shape_circle)
        monDate!!.setBackgroundResource(R.drawable.shape_circle)
        tueDate!!.setBackgroundResource(R.drawable.shape_circle)
        wedDate!!.setBackgroundResource(R.drawable.shape_circle)
        thuDate!!.setBackgroundResource(R.drawable.shape_circle)
        friDate!!.setBackgroundResource(R.drawable.shape_circle)
        satDate!!.setBackgroundResource(R.drawable.shape_circle)
    }

    private fun startingDay() {

        val todayCalendar = Calendar.getInstance()
        val todayDate = Date()
        todayCalendar.time = todayDate

        weekNo = todayCalendar[Calendar.WEEK_OF_YEAR]
        if (todayCalendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
            weekNo++
        }
        setColorDaySelected(todayCalendar[Calendar.DAY_OF_WEEK])
        setDateToCardViews(weekNo)

        val simpleDateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        textView_month!!.text = simpleDateFormat.format(todayDate)
    }

    private fun setOnDateClickListeners() {
        val date = Calendar.getInstance()
        date[Calendar.WEEK_OF_YEAR] = weekNo
        date.firstDayOfWeek = Calendar.SUNDAY

        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        sunDate.setOnClickListener {
            colorBlack()
            sunDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        }
        monDate.setOnClickListener {
            colorBlack()
            monDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        }
        tueDate.setOnClickListener {
            colorBlack()
            tueDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
        }
        wedDate.setOnClickListener {
            colorBlack()
            wedDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
        }
        thuDate.setOnClickListener {
            colorBlack()
            thuDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
        }
        friDate.setOnClickListener {
            colorBlack()
            friDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
        }
        satDate.setOnClickListener {
            colorBlack()
            satDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
            date[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
        }
    }

}