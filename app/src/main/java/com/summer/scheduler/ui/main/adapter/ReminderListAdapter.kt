package com.summer.scheduler.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.summer.scheduler.R
import com.summer.scheduler.data.model.ReminderItem

class ReminderListAdapter(private val reminderItems: ArrayList<ReminderItem>): RecyclerView.Adapter<ReminderListAdapter.ReminderItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_list_item, parent, false)
        return ReminderItemHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderItemHolder, position: Int) {
        holder.timings.text = reminderItems[position].timings
        holder.title.text = reminderItems[position].title
    }

    override fun getItemCount(): Int {
        return reminderItems.size
    }

    class ReminderItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val timings: TextView = itemView.findViewById(R.id.textView_reminderTimings)
        val title: TextView = itemView.findViewById(R.id.textView_reminderTitle)
    }

    fun addAll(items: ArrayList<ReminderItem>) {
        reminderItems.clear()
        reminderItems.addAll(items)
        notifyDataSetChanged()
    }

}