package com.summer.scheduler.ui.main.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_events.view.*

class NewEventBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var newEventViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        newEventViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        view.event_done.setOnClickListener {
            insertToDotoDatabase()
        }

        return view
    }

    private fun insertToDotoDatabase() {
        val eventTitle = editText_fragmentEvents_eventTitle.text.toString()
        val eventAgenda = editText_fragmentEvents_agenda.text.toString()
        val eventStart = editText_fragmentEvents_starts.text
        val eventEnd = editText_fragmentEvents_ends.text
        val eventPeople = editText_fragmentEvents_people.text.toString()
        val eventLink = editText_fragmentEvents_link.text.toString()
        val eventLocation = editText_fragmentEvents_location.text.toString()

        if(inputCheck(eventTitle, eventStart, eventEnd)) {
            val Event = ReminderEntity("newEvent",eventTitle,eventAgenda,Integer.parseInt(eventStart.toString()),Integer.parseInt(eventEnd.toString()),eventPeople,eventLink,eventLocation)
            newEventViewModel.addReminder(Event)
            Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(eventTitle: String, eventStart: Editable, eventEnd: Editable):Boolean {
        return !(TextUtils.isEmpty(eventTitle) && eventStart.isEmpty() && eventEnd.isEmpty())
    }
}
