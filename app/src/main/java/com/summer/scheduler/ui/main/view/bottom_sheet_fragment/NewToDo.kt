package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.android.synthetic.main.fragment_reminders.view.*
import kotlinx.coroutines.launch

class NewToDo(setDoneVisibility: Boolean) : BottomSheetDialogFragment() {

    private var setDoneVisibility: Boolean = true
    private var added = false
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)
        view.to_do_done.isVisible = setDoneVisibility
        view.to_do_done.setOnClickListener {
            insertToDoToDatabase()
        }

        return view
    }

    fun insertToDoToDatabase() :Boolean{
        val toDoItem = editText_fragmentEvents_reminder.text.toString()
        val date = editText_fragmentEvents_date.text

        return if (inputCheck(toDoItem, date)) {
            val toDo = ToDoEntity(0, toDoItem, date.toString().toInt(), false)

            lifecycleScope.launch {
                mainViewModel.addToDo(toDo)
            }

            added = true
            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()
            dismiss()
            true
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
            false
        }

    }

    private fun inputCheck(toDoItem: String, date: Editable): Boolean {
        return !(TextUtils.isEmpty(toDoItem) && date.isEmpty())
    }

    private var mListener: OnToDoAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
            .get(MainViewModel::class.java)

        if (context is OnToDoAddedListener) {
            mListener = context
        } else {
            throw RuntimeException(
                "$context must implement OnToDoAddedListener"
            )
        }
    }

    interface OnToDoAddedListener {
        fun onCloseToDoFragment(added: Boolean)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): NewToDo {
            val fragment = NewToDo(bundle.getBoolean("setDoneVisibility"))
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onCloseToDoFragment(added)
    }

    init {
        this.setDoneVisibility = setDoneVisibility
    }
}