package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.reminder_details_layout.*
import kotlinx.android.synthetic.main.reminder_details_layout.view.*

class EventView : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reminder_details_layout, container, false)

        view.textView_doneButton.setOnClickListener {
            textView_doneButton.setVisibility(View.INVISIBLE)


        }
        return view;
    }
}