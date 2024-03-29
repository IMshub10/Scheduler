package com.summer.scheduler.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.`interface`.ReminderRecyclerViewItemClickListener
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter
import com.summer.scheduler.ui.main.adapter.ToDoListAdapter
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.AddOption
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.EventView
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.NewEvent
import com.summer.scheduler.ui.main.view.bottom_sheet_fragment.NewToDo
import com.summer.scheduler.ui.main.view.calendardialog.DatePickerDialog
import com.summer.scheduler.ui.main.view.calendardialog.DatePickerDialogBoxListener
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import com.summer.scheduler.ui.main.viewstate.MainState
import com.summer.scheduler.utils.SwipeItemTouchHelper
import com.summer.scheduler.utils.listeners.Swipe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.schedule_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
    NewEvent.OnEventAddedListener,
    NewToDo.OnToDoAddedListener,
    DatePickerDialogBoxListener,
    Swipe {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var reminderAdapter: ReminderListAdapter
    private lateinit var toDoAdapter: ToDoListAdapter
    private var weekNo = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_mainActivity)
        setupUI()
        startingDay()
        setupViewModel()
        setOnMonthClickListener()
        setOnDateClickListeners()
        newItemClickListener()
        observableViewModel()
    }

    override fun onStart() {
        super.onStart()
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val string = formatter.format(date)

        fetchData(string)
    }

    private fun fetchData(string: String) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.FetchReminders(string.toInt()))
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
                            val date = Calendar.Builder()
                                .setDate(selectedYear, selectedMonth - 1, selectedDate)
                                .set(Calendar.WEEK_OF_YEAR, weekNo)
                                .build()
                            date[Calendar.WEEK_OF_YEAR] = weekNo
                            date.firstDayOfWeek = Calendar.SUNDAY
                            colorBlack()
                            changeList(it.day, weekNo)
                            when (it.day) {
                                1 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
                                    sunDate.alpha = 1.0f
                                    textView_sunday!!.alpha = 1.0f
                                    sunDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                2 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                                    monDate.alpha = 1.0f
                                    textView_monday!!.alpha = 1.0f
                                    monDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                3 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
                                    tueDate.alpha = 1.0f
                                    textView_tuesday!!.alpha = 1.0f
                                    tueDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                4 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
                                    wedDate.alpha = 1.0f
                                    textView_wednesday!!.alpha = 1.0f
                                    wedDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                5 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
                                    thuDate.alpha = 1.0f
                                    textView_thursday!!.alpha = 1.0f
                                    thuDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                6 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
                                    friDate.alpha = 1.0f
                                    textView_friday!!.alpha = 1.0f
                                    friDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }
                                7 -> {
                                    date[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
                                    satDate.alpha = 1.0f
                                    textView_saturday!!.alpha = 1.0f
                                    satDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                                }

                            }
                        }
                        is MainState.OpenSwitchBottomSheet -> {
                            openSwitchFragment()
                        }
                    }
                }
            }
        }
    }

    private var selectedDate = Calendar.getInstance()[Calendar.DATE]
    private var selectedMonth = Calendar.getInstance()[Calendar.MONTH] + 1
    private var selectedYear = Calendar.getInstance()[Calendar.YEAR]

    private fun changeList(day: Int, weekNo: Int) {
        val calendar = Calendar.Builder()
            .setCalendarType("gregorian")
            .setDate(selectedYear, selectedMonth - 1, selectedDate)
            .build()

        calendar[Calendar.WEEK_OF_YEAR] = weekNo
        calendar.firstDayOfWeek = Calendar.SUNDAY
        val diff = day - calendar[Calendar.DAY_OF_WEEK]
        calendar.add(Calendar.DATE, diff)

        var dString = "${calendar[Calendar.DATE]}"
        var mString = "${calendar[Calendar.MONTH] + 1}"
        val yString = "${calendar[Calendar.YEAR]}"

        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) dString = "0$dString"
        if (calendar.get(Calendar.MONTH) + 1 < 10) mString = "0$mString"

        val hSelectedDay = "$yString$mString$dString"

        fetchData(hSelectedDay)

        val date = calendar.time
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val text = formatter.format(date)

        textView_month_year!!.text = text

        Log.e("dayOfWeek", "${calendar[Calendar.DAY_OF_WEEK]}")
        Log.e(
            "changeList",
            "${calendar[Calendar.DATE]} ${calendar[Calendar.MONTH] + 1} ${calendar[Calendar.YEAR]}"
        )
        Log.e("selectedItems", "$selectedYear $selectedMonth $selectedDate")
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
        Log.e("getAllReminders", "Here")
        if (reminders.isEmpty()) {
            textView_todayEmpty.visibility = View.VISIBLE
        } else {
            textView_todayEmpty.visibility = View.GONE
        }
        reminderAdapter.submitList(reminders)
    }

    private fun getAllToDos(toDos: List<ToDoEntity>) {
        Log.e("getAllToDos", "Here")
        if (toDos.isEmpty()) {
            textView_to_do_Empty.visibility = View.VISIBLE
        } else {
            textView_to_do_Empty.visibility = View.GONE
        }
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
    }


    private fun returnFirstTwoChars(d: String): String {
        return d.substring(0, 2)
    }

    private fun setDateToCardViews(weekNo: Int) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val cal = Calendar.Builder()
            .setDate(selectedYear, selectedMonth - 1, selectedDate)
            .set(Calendar.WEEK_OF_YEAR, weekNo)
            .build()
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
                sunDate.alpha = 1.0f
                textView_sunday.alpha = 1.0f
            }
            2 -> {
                colorBlack()
                monDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                monDate.alpha = 1.0f
                textView_monday.alpha = 1.0f
            }
            3 -> {
                colorBlack()
                tueDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                tueDate.alpha = 1.0f
                textView_tuesday.alpha = 1.0f
            }
            4 -> {
                colorBlack()
                wedDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                wedDate.alpha = 1.0f
                textView_wednesday.alpha = 1.0f
            }
            5 -> {
                colorBlack()
                thuDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                thuDate.alpha = 1.0f
                textView_thursday.alpha = 1.0f
            }
            6 -> {
                colorBlack()
                friDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                friDate.alpha = 1.0f
                textView_friday.alpha = 1.0f
            }
            7 -> {
                colorBlack()
                satDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
                textView_saturday.alpha = 1.0f
                satDate.alpha = 1.0f
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

        sunDate!!.alpha = 0.35f
        monDate!!.alpha = 0.35f
        tueDate!!.alpha = 0.35f
        wedDate!!.alpha = 0.35f
        thuDate!!.alpha = 0.35f
        friDate!!.alpha = 0.35f
        satDate!!.alpha = 0.35f

        textView_sunday!!.alpha = 0.35f
        textView_monday!!.alpha = 0.35f
        textView_tuesday!!.alpha = 0.35f
        textView_wednesday!!.alpha = 0.35f
        textView_thursday!!.alpha = 0.35f
        textView_friday!!.alpha = 0.35f
        textView_saturday!!.alpha = 0.35f
    }

    private fun startingDay() {

        val todayCalendar = Calendar.Builder()
            .setDate(selectedYear, selectedMonth - 1, selectedDate)
            .set(Calendar.WEEK_OF_YEAR, weekNo)
            .build()
        val todayDate = Date()
        todayCalendar.time = todayDate
        weekNo = todayCalendar[Calendar.WEEK_OF_YEAR]
        if (todayCalendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
            weekNo++
        }
        setColorDaySelected(todayCalendar[Calendar.DAY_OF_WEEK])
        setDateToCardViews(weekNo)
        val simpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        textView_month_year!!.text = simpleDateFormat.format(todayDate)
    }

    private fun setupRecyclerViews() {
        to_do_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        today_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        to_do_recyclerView.adapter = toDoAdapter
        today_recyclerView.adapter = reminderAdapter

        reminderAdapter.setOnEventClickListener(
            object: ReminderRecyclerViewItemClickListener {
                override fun onEventClick(itemView: View, layoutPosition: Int) {
                    val selectedReminderEntity = reminderAdapter.getItem(layoutPosition)
                    val reminderDetailsSheet = EventView(selectedReminderEntity)
                    reminderDetailsSheet.show(supportFragmentManager, reminderDetailsSheet.tag)
                }

            }
        )

        val reminderSwipeHelperCallback: ItemTouchHelper.Callback = SwipeItemTouchHelper(
            this,
            0,
            ItemTouchHelper.LEFT,
            this
        )
        val reminderItemTouchHelper = ItemTouchHelper(reminderSwipeHelperCallback)
        reminderItemTouchHelper.attachToRecyclerView(today_recyclerView)

        val toDoSwipeHelperCallback: ItemTouchHelper.Callback = SwipeItemTouchHelper(
            this,
            0,
            ItemTouchHelper.LEFT,
            this
        )
        val toDoItemTouchHelper = ItemTouchHelper(toDoSwipeHelperCallback)
        toDoItemTouchHelper.attachToRecyclerView(to_do_recyclerView)

    }

    fun setOnMonthClickListener() {
        textView_month_year.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromDatePicker)
            }
//            openDatePickerDialog()
        }
    }

    private fun setOnDateClickListeners() {
        val date = Calendar.Builder()
            .setDate(selectedYear, selectedMonth - 1, selectedDate)
            .set(Calendar.WEEK_OF_YEAR, weekNo)
            .build()
        date[Calendar.WEEK_OF_YEAR] = weekNo
        date.firstDayOfWeek = Calendar.SUNDAY

        sunDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(1))
//            sunDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
            }
        }
        monDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(2))
//            monDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }
        }
        tueDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(3))
//            tueDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
            }
        }
        wedDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(4))
//            wedDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
            }
        }
        thuDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(5))
//            thuDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
            }
        }
        friDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(6))
//            friDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
            }
        }
        satDate.setOnClickListener {
//            colorBlack()
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SelectDateFromHorizontalPicker(7))
//            satDate!!.setBackgroundResource(R.drawable.shape_circle_selected)
//            date[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
            }
        }
    }

    private fun newItemClickListener() {
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


    override fun sendDateInfo(dateString: String?, weekNo: Int, date: Date?) {
        Log.e("weekNumber", weekNo.toString())
        setSelected(dateString!!)
        Log.e("sendDateInfo", dateString)
        this.weekNo = weekNo
        val c = Calendar.Builder()
            .setDate(selectedYear, selectedMonth - 1, selectedDate)
            .set(Calendar.WEEK_OF_YEAR, weekNo)
            .build()
        c.time = date!!
        val simpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        textView_month_year!!.text = simpleDateFormat.format(date)
        if (c[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
            this.weekNo++
            Log.e("weekNumber", "true")
        }
        setDateToCardViews(weekNo)
        setColorDaySelected(c[Calendar.DAY_OF_WEEK])
        Log.e("weekNumber", weekNo.toString())
        fetchData(dateString)
    }

    private fun setSelected(dateString: String) {
        selectedYear = dateString.substring(0, 4).toInt()
        selectedMonth = dateString.substring(4, 6).toInt()
        selectedDate = dateString.substring(6, 8).toInt()
    }

    override fun onCloseDatePickerFragment() {
        mainViewModel.setIdleState()
    }

    override fun onCloseReminderFragment(added: Boolean) {
        if (!added)
            mainViewModel.setIdleState()
    }

    override fun onCloseToDoFragment(added: Boolean) {
        if (!added)
            mainViewModel.setIdleState()
    }

    override fun rightSwipeDelete(position: Int, recyclerId: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addButton) {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.SwitchBetweenReminderToDo)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}