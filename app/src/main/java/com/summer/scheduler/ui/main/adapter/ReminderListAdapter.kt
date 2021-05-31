package com.summer.scheduler.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter.*

class ReminderListAdapter(private val context: Context) :
    ListAdapter<ReminderEntity, ReminderItemHolder>(DIFF_CALLBACK) {
    class ReminderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timings: TextView = itemView.findViewById(R.id.textView_reminderTimings)
        val title: TextView = itemView.findViewById(R.id.textView_reminderTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_list_item, parent, false)
        return ReminderItemHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderItemHolder, position: Int) {
        val reminder = getItem(position)

        var sHour = ""
        var sMin = ""
        var eHour = ""
        var eMin = ""

        if (reminder.startHour < 10) {
            sHour = "0"
        }
        if (reminder.startMinute < 10) {
            sMin = "0"
        }
        if (reminder.endHour < 10) {
            eHour = "0"
        }
        if (reminder.endMinute < 10) {
            eMin = "0"
        }

        sHour += reminder.startHour
        sMin += reminder.startMinute
        eHour += reminder.endHour
        eMin += reminder.endMinute

        val time = "$sHour:$sMin-$eHour:$eMin"

        holder.timings.text = time
        holder.title.text = reminder.event
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ReminderEntity> =
            object : DiffUtil.ItemCallback<ReminderEntity>() {
                override fun areItemsTheSame(
                    oldItem: ReminderEntity,
                    newItem: ReminderEntity
                ): Boolean {
                    return oldItem.key == newItem.key
                }

                override fun areContentsTheSame(
                    oldItem: ReminderEntity,
                    newItem: ReminderEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}