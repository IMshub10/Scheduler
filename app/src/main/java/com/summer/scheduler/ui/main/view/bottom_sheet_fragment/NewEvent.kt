package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_events.view.*
import kotlinx.android.synthetic.main.fragment_reminders.view.*
import java.util.*

class NewEvent(setDoneVisibility: Boolean) : BottomSheetDialogFragment() {

    var setDoneVisibility: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        view.event_done.isVisible = setDoneVisibility
        view.event_done.setOnClickListener {
            insertToDoToDatabase()
        }

        return view
    }

     fun insertToDoToDatabase() :Boolean{
        val eventTitle = editText_fragmentEvents_eventTitle.text.toString()
        val eventAgenda = editText_fragmentEvents_agenda.text.toString()
        val eventStart = editText_fragmentEvents_starts.text
        val eventEnd = editText_fragmentEvents_ends.text
        val eventPeople = editText_fragmentEvents_people.text.toString()
        val eventLink = editText_fragmentEvents_link.text.toString()
        val eventLocation = editText_fragmentEvents_location.text.toString()

        if(inputCheck(eventTitle, eventStart, eventEnd)) {
            val c: Calendar = Calendar.getInstance()
            var dString = "${c.get(Calendar.DAY_OF_MONTH)}"
            var mString = "${c.get(Calendar.MONTH)+1}"

            if (c.get(Calendar.DAY_OF_MONTH) < 10) dString = "0$dString"
            if (c.get(Calendar.MONTH) + 1 < 10) mString = "0$mString"
            val yString = "${c.get(Calendar.YEAR)}"

            val day = "$yString$mString$dString"

            val event = ReminderEntity(0,eventTitle,eventAgenda,Integer.parseInt(eventStart.toString()),Integer.parseInt(eventEnd.toString()),eventPeople,eventLink,eventLocation, day.toInt())
            mListener?.onEventAdded(event)
            Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
            dismiss()
            return true
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun inputCheck(eventTitle: String, eventStart: Editable, eventEnd: Editable):Boolean {
        return !(TextUtils.isEmpty(eventTitle) && eventStart.isEmpty() && eventEnd.isEmpty())
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
        fun onEventAdded(event: ReminderEntity)
        fun onCloseReminderFragment()
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onCloseReminderFragment()
    }
    init {
        this.setDoneVisibility = setDoneVisibility
    }
}
