package com.summer.scheduler.ui.main.view.calendardialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.summer.scheduler.R
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog : AppCompatDialogFragment() {
    private var dateString = ""
    private var weekNo = 0
    private var listener: DatePickerDialogBoxListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.component_scheduler_calendar, null)
        val datePicker = view.findViewById<DatePicker>(R.id.datePickerDialog)
        datePicker.firstDayOfWeek = Calendar.SUNDAY
        datePicker.setOnDateChangedListener { _, i, i1, i2 ->
            val cal = Calendar.Builder()
                .setDate(i, i1, i2)
                .build()
            cal[Calendar.DAY_OF_WEEK]
            val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            dateString = sdf.format(cal.time)
            weekNo = cal[Calendar.WEEK_OF_YEAR]
            Log.e("DatePickerDialog->onDateChange", "$i $i1 $i2")
            Log.e("DatePickerDialog->onDateChange", "${cal[Calendar.DAY_OF_WEEK]} ${cal[Calendar.DAY_OF_WEEK_IN_MONTH]}")
            if (cal[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY){
                weekNo++
            }
            listener!!.sendDateInfo(dateString, weekNo, cal.time)
            dismiss()
        }
        builder.setView(view)
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DatePickerDialogBoxListener) {
            listener = context
        } else {
            throw ClassCastException(
                "$context must implement CalendarDialogBoxListener"
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DatePickerDialog {
            val fragment = DatePickerDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener?.onCloseDatePickerFragment()
    }
}