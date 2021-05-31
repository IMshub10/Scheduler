package com.summer.scheduler.ui.main.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mCalender = Calendar.getInstance()
        val year = mCalender[Calendar.YEAR]
        val month = mCalender[Calendar.MONTH]
        val dayOfMonth = mCalender[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireActivity(), activity as OnDateSetListener?, year, month, dayOfMonth)
    }
}