package com.summer.scheduler.ui.main.view

import AddOptionBottomSheetFragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addButton -> {
                val addOptionSheet = AddOptionBottomSheetFragment()
                addOptionSheet.show(supportFragmentManager,addOptionSheet.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}