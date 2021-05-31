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
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.android.synthetic.main.fragment_reminders.view.*
import kotlinx.android.synthetic.main.schedule_main.view.*

class NewToDoBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var newToDoViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)

        newToDoViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        view.to_do_done.setOnClickListener {
            insertToDotoDatabase()
        }

        return view
    }

    private fun insertToDotoDatabase() {
        val toDoItem = editText_fragmentEvents_reminder.text.toString()
        val date = editText_fragmentEvents_date.text

        if(inputCheck(toDoItem, date)) {
            val ToDo = ToDoEntity("ToDoItem",toDoItem,Integer.parseInt(date.toString()),true)
            newToDoViewModel.addToDo(ToDo)
            Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(toDoItem: String, date: Editable):Boolean {
        return !(TextUtils.isEmpty(toDoItem) && date.isEmpty())
    }
}