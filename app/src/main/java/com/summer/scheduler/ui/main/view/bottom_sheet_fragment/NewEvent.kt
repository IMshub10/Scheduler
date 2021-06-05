package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.ui.main.view.time_picker_dialog.TimePickerDialog
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_events_new.*
import kotlinx.android.synthetic.main.fragment_events_new.view.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewEvent(setDoneVisibility: Boolean) : BottomSheetDialogFragment() {

    private var setDoneVisibility: Boolean = true
    private var added = false
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events_new, container, false)
        view.event_new_done.isVisible = setDoneVisibility
        view.event_new_done.setOnClickListener {
            insertToDoToDatabase()
        }

        view.textView_newEventFragmentFrom.setOnClickListener {
            requireActivity().supportFragmentManager.let {
                TimePickerDialog.newInstance(Bundle(), true).apply {
                    show(it, tag)
                }
            }
        }

        view.textView_newEventFragmentTo.setOnClickListener {
            requireActivity().supportFragmentManager.let {
                TimePickerDialog.newInstance(Bundle(), false).apply {
                    show(it, tag)
                }
            }
        }

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        mainViewModel.startTimeArray.observe(this, {
            Log.e("observer", "Here start")
            for (i in it) {
                Log.e("start for", "$i")
            }
            onTimePicked(it, textView_newEventFragmentFrom)
        })

        mainViewModel.stopTimeArray.observe(this, {
            Log.e("observer", "Here stop")
            for (i in it) {
                Log.e("stop for", "$i")
            }
            onTimePicked(it, textView_newEventFragmentTo)
        })

        return view
    }

     fun insertToDoToDatabase() :Boolean{
         val eventTitle = editText_fragmentEventsNew_eventTitle.text.toString()
         val eventAgenda = editText_fragmentEventsNew_agenda.text.toString()
         val eventStart = textView_newEventFragmentFrom.text.toString()
         val eventEnd = textView_newEventFragmentTo.text.toString()
         val eventDate = editText_fragmentEventsNew_date.text.toString()
         val eventPeople = editText_fragmentEventsNew_people.text.toString()
         val eventLink = editText_fragmentEventsNew_link.text.toString()
         val eventLocation = editText_fragmentEventsNew_location.text.toString()

         return if(inputCheck(eventTitle, eventStart, eventEnd, eventDate) &&
             eventDate != "" && eventStart != "start time" && eventEnd != "stop time") {
             val event = ReminderEntity(
                 0,eventTitle,eventAgenda,eventStart,
                 eventEnd,eventPeople,eventLink,
                 eventLocation, eventDate.toInt()
             )

             lifecycleScope.launch {
                 mainViewModel.addReminder(event)
             }

             added = true
             Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
             dismiss()
             true

         } else {
             Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
             false
         }
    }

    private fun inputCheck(eventTitle: String, eventStart: String, eventEnd: String, eventDate: String):Boolean {
        return !((eventTitle.isEmpty() || eventTitle.isBlank()) &&
                (eventStart.isEmpty() || eventStart.isBlank()) &&
                (eventEnd.isEmpty() || eventEnd.isBlank()) &&
                (eventDate.isEmpty() || eventDate.isBlank()))
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): NewEvent {
            val fragment = NewEvent(bundle.getBoolean("setDoneVisibility"))
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mListener: OnEventAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEventAddedListener) {
            mListener = context
        } else {
            throw RuntimeException(
                "$context must implement OnEventAddedListener"
            )
        }
    }

    interface OnEventAddedListener {
        fun onCloseReminderFragment(added: Boolean)
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onCloseReminderFragment(added)
        mainViewModel.startTimeArray.value = arrayOf(-1,-1)
        mainViewModel.stopTimeArray.value = arrayOf(-1,-1)
    }
    init {
        this.setDoneVisibility = setDoneVisibility
    }

    private fun onTimePicked(time: Array<Int>, textView: TextView) {
        if (time[0] == -1 && time[1] == -1) {
            var string = ""
            if (textView.id == textView_newEventFragmentFrom.id) {
                string = "start time"
            } else if (textView.id == textView_newEventFragmentTo.id) {
                string = "stop time"
            }
            textView.text = string
        }
        else {
            val hr = time[0]
            val min = time[1]
            val date = Calendar.Builder()
                .set(Calendar.HOUR, hr)
                .set(Calendar.MINUTE, min)
                .build().time

            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateString = format.format(date)
            textView.text = dateString
        }
    }
}
