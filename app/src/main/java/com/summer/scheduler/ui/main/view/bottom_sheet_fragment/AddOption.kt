package com.summer.scheduler.ui.main.view.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R
import kotlinx.android.synthetic.main.events_and_reminders.*

class AddOption : BottomSheetDialogFragment() {

    private lateinit var toggle: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.events_and_reminders, container, false)

        materialButton_toDo.isSelected = true
        materialButton_toDo.setOnClickListener {
            view: View -> view.findNavController().navigate(R.id.action_addOptionBottomSheetFragment_to_newToDoBottomSheetFragment2)
        }
        materialButton_today.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.action_addOptionBottomSheetFragment_to_newEventBottomSheetFragment)
        }

        return view;
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