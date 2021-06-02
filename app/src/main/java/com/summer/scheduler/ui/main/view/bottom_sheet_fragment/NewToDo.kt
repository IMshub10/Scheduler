package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ToDoEntity
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.android.synthetic.main.fragment_reminders.view.*

class NewToDo : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)

        view.to_do_done.setOnClickListener {
            insertToDoToDatabase()
        }

        return view
    }

    private fun insertToDoToDatabase() {
        val toDoItem = editText_fragmentEvents_reminder.text.toString()
        val date = editText_fragmentEvents_date.text

        if(inputCheck(toDoItem, date)) {
            val toDo = ToDoEntity(0,toDoItem,Integer.parseInt(date.toString()),false)
            mListener?.onToDoAdded(toDo)
            Toast.makeText(requireContext(), "Successfully Added",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(toDoItem: String, date: Editable):Boolean {
        return !(TextUtils.isEmpty(toDoItem) && date.isEmpty())
    }

    private var mListener: OnToDoAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnToDoAddedListener) {
            mListener = context
        } else {
            throw RuntimeException(
                "$context must implement OnToDoAddedListener"
            )
        }
    }

    interface OnToDoAddedListener {
        fun onToDoAdded(toDo: ToDoEntity)
        fun onCloseToDoFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): NewToDo {
            val fragment = NewToDo()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onCloseToDoFragment()
    }
}