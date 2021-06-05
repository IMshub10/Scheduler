package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_events_new.*
import kotlinx.android.synthetic.main.reminder_details_layout.*
import kotlinx.android.synthetic.main.reminder_details_layout.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventView(data: ReminderEntity) : BottomSheetDialogFragment() {


    private lateinit var mainViewModel : MainViewModel
    private val eventDetails: ReminderEntity = data
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

        mainViewModel = ViewModelProvider(requireActivity(),
            ViewModelFactory(requireActivity().application))
            .get(MainViewModel::class.java)


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

            GlobalScope.launch {
                updateReminder()
            }

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText_reminderDetailsTitle.text = Editable.Factory.getInstance().newEditable(eventDetails.event)
        editText_reminderDetailsStartTime.text = Editable.Factory.getInstance().newEditable(eventDetails.start)
        editText_reminderDetailsEndTime.text = Editable.Factory.getInstance().newEditable(eventDetails.end)
        editText_reminderDetails.text = Editable.Factory.getInstance().newEditable(eventDetails.agenda)
        editText_reminderDetailsLocation.text = Editable.Factory.getInstance().newEditable(eventDetails.location)
        editText_reminderDetailsLinks.text = Editable.Factory.getInstance().newEditable(eventDetails.link)
        editText_reminderDetailsUsers.text = Editable.Factory.getInstance().newEditable(eventDetails.people)
    }

    private suspend fun updateReminder(){
        val eventTitle = editText_fragmentEventsNew_eventTitle.text.toString()
        val eventAgenda = editText_fragmentEventsNew_agenda.text.toString()
        val eventStart = textView_newEventFragmentFrom.text.toString()
        val eventEnd = textView_newEventFragmentTo.text.toString()
        val eventDate = editText_fragmentEventsNew_date.text.toString()
        val eventPeople = editText_fragmentEventsNew_people.text.toString()
        val eventLink = editText_fragmentEventsNew_link.text.toString()
        val eventLocation = editText_fragmentEventsNew_location.text.toString()

        if(inputCheck(eventTitle, eventStart, eventEnd, eventDate)) {

            val updateEvent = ReminderEntity(eventDetails.key,eventTitle,eventAgenda,eventStart,
                eventEnd,eventPeople,eventLink,
                eventLocation, eventDate)

            mainViewModel.viewModelScope.launch {
                mainViewModel.userIntent.send(MainIntent.UpdateReminder(updateEvent))
            }

            eventUpdateViewModel.updateReminder(updateEvent)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Please fill out mandatory fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(eventTitle: String, eventStart: String, eventEnd: String, eventDate: String):Boolean {
        return !((eventTitle.isEmpty() || eventTitle.isBlank()) &&
                (eventStart.isEmpty() || eventStart.isBlank()) &&
                (eventEnd.isEmpty() || eventEnd.isBlank()) &&
                (eventDate.isEmpty() || eventDate.isBlank()))
    }
}