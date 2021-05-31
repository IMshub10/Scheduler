package com.summer.scheduler.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.scheduler.R

class MainSchedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_main)

        val newToDo = findViewById<TextView>(R.id.to_do_newItem)
        newToDo.setOnClickListener{
            val newToDoSheet = NewToDoBottomSheetFragment()
            newToDoSheet.show(supportFragmentManager,newToDoSheet.tag)
        }

        val newEvent = findViewById<TextView>(R.id.today_newEvent)
        newEvent.setOnClickListener{
            val newEventSheet = NewEventBottomSheetFragment()
            newEventSheet.show(supportFragmentManager,newEventSheet.tag)
        }
    }
}