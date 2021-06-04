package com.summer.scheduler.ui.main.view.time_picker_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDialogFragment
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.component_time_picker.*
import java.text.SimpleDateFormat
import java.util.*

class TimePickerDialog: AppCompatDialogFragment() {

    private var selectedHour: Int
    private var selectedMin: Int

    init {
        val time = Calendar.getInstance().time
        val format = SimpleDateFormat("HHmm", Locale.getDefault())
        val timeString = format.format(time)
        selectedHour = timeString.substring(0..2).toInt()
        selectedMin = timeString.substring(2..4).toInt()
        Log.e("TimePickerDialog", "timeString: $timeString")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.component_time_picker, null)
        val timePicker = timePickerDialog

        timePicker.setOnTimeChangedListener { _, i, i2 ->
            selectedHour = i
            selectedMin = i2
        }
        builder.setView(view)
        return builder.create()
    }

    fun getTime(): Array<Int> {
        return arrayOf(selectedHour, selectedMin)
    }
}