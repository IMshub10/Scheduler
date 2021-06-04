package com.summer.scheduler.ui.main.view.time_picker_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.summer.scheduler.R
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.component_time_picker.view.*
import java.text.SimpleDateFormat
import java.util.*

class TimePickerDialog: AppCompatDialogFragment() {

    private var selectedHour: Int
    private var selectedMin: Int

    init {
        val time = Calendar.getInstance().time
        val format = SimpleDateFormat("HHmm", Locale.getDefault())
        val timeString = format.format(time)
        selectedHour = timeString.substring(0, 2).toInt()
        selectedMin = timeString.substring(2, 4).toInt()
        Log.e("TimePickerDialog", "timeString: $timeString")
    }

    private lateinit var doneButton: Button
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.component_time_picker, null)
        val timePicker = view.timePickerDialog

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        doneButton = view.button_timePickerSet

        doneButton.setOnClickListener {
            if (mStart) {
                Log.e("doneButtonOnClick", "Here start")
                mainViewModel.startTimeArray.value = getTime()
            }
            else {
                Log.e("doneButtonOnClick", "Here stop")
                mainViewModel.stopTimeArray.value = getTime()
            }
            dismiss()
        }

        timePicker.setOnTimeChangedListener { _, i, i2 ->
            selectedHour = i
            selectedMin = i2
            Log.e("timePicker.setOnTimeChangedListener", "$selectedHour $selectedMin")
        }
        builder.setView(view)
        return builder.create()
    }

    private fun getTime(): Array<Int> {
        return arrayOf(selectedHour, selectedMin)
    }

    companion object {
        private var mStart = true
        fun newInstance(bundle: Bundle, start: Boolean): TimePickerDialog {
            val fragment = TimePickerDialog()
            fragment.arguments = bundle
            mStart = start
            return fragment
        }
    }
}