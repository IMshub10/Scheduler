
package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.events_and_reminders.*
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_reminders.*

class AddOption : BottomSheetDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.events_and_reminders, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        materialButton_toDo!!.isSelected = true

        loadToDoFragment()
        materialButton_toDo!!.setOnClickListener {
            loadToDoFragment()
        }
        materialButton_today!!.setOnClickListener {
            loadNewEventFragment()
        }
    }



    private fun loadToDoFragment(){
        val newToDo = NewToDo(false)
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.toggle_fragment_container, newToDo)
        ft.commit()
        newToDo.to_do_done.visibility = View.GONE
        textView_done_eventsAndReminders.setOnClickListener{
            newToDo.insertToDoToDatabase()
            dismiss()
        }

    }
    private fun loadNewEventFragment(){
        val newEvent = NewEvent(false)
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.toggle_fragment_container, newEvent)
        ft.commit()
        newEvent.event_done.visibility = View.GONE
        textView_done_eventsAndReminders.setOnClickListener{
            newEvent.insertToDoToDatabase()
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): AddOption {
            val fragment = AddOption()
            fragment.arguments = bundle
            return fragment
        }
    }
}