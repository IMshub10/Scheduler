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
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.android.synthetic.main.fragment_reminders.view.*

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

        newToDoViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
            .get(MainViewModel::class.java)

        view.to_do_done.setOnClickListener {
            insertToDoToDatabase()
        }

        return view
    }

    private fun insertToDoToDatabase() {
        val toDoItem = editText_fragmentEvents_reminder.text.toString()
        val date = editText_fragmentEvents_date.text

        if(inputCheck(toDoItem, date)) {
            val toDo = ToDoEntity(0,toDoItem,Integer.parseInt(date.toString()),true)
            newToDoViewModel.addToDo(toDo)
            Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(toDoItem: String, date: Editable):Boolean {
        return !(TextUtils.isEmpty(toDoItem) && date.isEmpty())
    }
}