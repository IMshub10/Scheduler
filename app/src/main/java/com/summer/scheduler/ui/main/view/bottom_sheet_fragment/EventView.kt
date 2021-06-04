package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.reminder_details_layout.*
import kotlinx.android.synthetic.main.reminder_details_layout.view.*
import java.util.*

class EventView : BottomSheetDialogFragment() {

   
    private lateinit var eventUpdateViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reminder_details_layout, container, false)

        eventUpdateViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        view.imageView_editReminder.setOnClickListener {

            imageView_editReminder.visibility = View.INVISIBLE
            textView_doneEditButton.visibility = View.VISIBLE
            editText_reminderTitleUpdate.visibility = View.VISIBLE

            editText_reminderTitleUpdate.text = editText_reminderDetailsTitle.text
            editText_reminderDetailsTitle.text = Editable.Factory.getInstance().newEditable("Edit Event")

            editText_reminderTitleUpdate.isEnabled = true
            editText_reminderDetailsStartTime.isEnabled = true
            editText_reminderDetailsEndTime.isEnabled = false
            editText_reminderDetails.isEnabled = true
            editText_reminderDetailsLocation.isEnabled = true
            editText_reminderDetailsLinks.isEnabled = true
            editText_reminderDetailsUsers.isEnabled = true

            editText_reminderTitleUpdate.requestFocus()
        }

        textView_doneEditButton.setOnClickListener {

            updateReminder()
            editText_reminderDetailsTitle.text = editText_reminderTitleUpdate.text

            imageView_editReminder.visibility = View.VISIBLE
            textView_doneEditButton.visibility = View.INVISIBLE
            editText_reminderTitleUpdate.visibility = View.GONE

            editText_reminderDetailsStartTime.isEnabled = false
            editText_reminderDetailsEndTime.isEnabled = false
            editText_reminderDetails.isEnabled = false
            editText_reminderDetailsLocation.isEnabled = false
            editText_reminderDetailsLinks.isEnabled = false
            editText_reminderDetailsUsers.isEnabled = false


        }
        return view;
    }

    private suspend fun updateReminder(){
        val title = editText_reminderTitleUpdate.text.toString()
        val startTime = editText_reminderDetailsStartTime.text
        val endTime = editText_reminderDetailsEndTime.text
        val details = editText_reminderDetails.text.toString()
        val location = editText_reminderDetailsLocation.text.toString()
        val links = editText_reminderDetailsLinks.text.toString()
        val people = editText_reminderDetailsUsers.text.toString()

        if(inputCheck(title., startTime, endTime)) {
            val c: Calendar = Calendar.getInstance()
            var dString = "${c.get(Calendar.DAY_OF_MONTH)}"
            var mString = "${c.get(Calendar.MONTH)}"

            if (c.get(Calendar.DAY_OF_MONTH) < 10) dString = "0$dString"
            if (c.get(Calendar.MONTH) < 10) mString = "0$mString"
            val yString = "${c.get(Calendar.YEAR)}"

            val day = "$yString$mString$dString"
            val updateEvent = ReminderEntity(0, title, details, Integer.parseInt(startTime.toString()),
                                Integer.parseInt(endTime.toString()),people,links,location, day.toInt())
            eventUpdateViewModel.updateReminder(updateEvent)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Please fill out mandatory fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(eventTitle: String, eventStart: Editable, eventEnd: Editable):Boolean {
        return !(TextUtils.isEmpty(eventTitle) && eventStart.isEmpty() && eventEnd.isEmpty())
    }
}