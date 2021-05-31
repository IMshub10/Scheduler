package com.summer.scheduler.ui.main.view.calendardialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.summer.scheduler.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarDialog : AppCompatDialogFragment() {
    var dateString = ""
    var weekNo = 0
    private var listener: CalendarDialogBoxListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.component_scheduler_calendar, null)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarDialog)
        calendarView.firstDayOfWeek = Calendar.SUNDAY
        calendarView.setOnDateChangeListener { calendarView, i, i1, i2 ->
            val cal = Calendar.getInstance()
            cal[i, i1] = i2
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateString = sdf.format(cal.time)
            weekNo = cal[Calendar.WEEK_OF_YEAR]
            listener!!.sendDateInfo(dateString, weekNo, cal.time)
            dismiss()
        }
        builder.setView(view)
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            targetFragment as CalendarDialogBoxListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement CalendarDialogBoxListener")
        }
    }
}