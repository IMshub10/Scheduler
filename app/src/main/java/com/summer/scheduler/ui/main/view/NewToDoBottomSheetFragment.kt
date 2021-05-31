package com.summer.scheduler.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.fragment_reminders.view.*
import kotlinx.android.synthetic.main.schedule_main.view.*

class NewToDoBottomSheetFragment : BottomSheetDialogFragment() {

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
            insertToDotoDatabase()
        }

        return view
    }

    private fun insertToDotoDatabase() {

    }
}