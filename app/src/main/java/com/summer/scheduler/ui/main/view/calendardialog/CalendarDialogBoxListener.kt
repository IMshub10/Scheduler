package com.summer.scheduler.ui.main.view.calendardialog

import java.util.*

interface CalendarDialogBoxListener {
    fun sendDateInfo(dateString: String?, weekNo: Int, date: Date?)
}