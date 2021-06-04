package com.summer.scheduler.ui.main.view.calendardialog

import java.util.*

interface DatePickerDialogBoxListener {
    fun sendDateInfo(dateString: String?, weekNo: Int, date: Date?)
    fun onCloseDatePickerFragment()
}