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
import com.summer.scheduler.ui.main.`interface`.ReminderRecyclerViewItemClickListener
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter.*
import kotlinx.android.synthetic.main.reminder_list_item.view.*

class ReminderListAdapter(private val context: Context) :
    ListAdapter<ReminderEntity, ReminderItemHolder>(DIFF_CALLBACK) {

    private var reminderRecyclerViewItemClickListener: ReminderRecyclerViewItemClickListener? = null
    private var reminderRecyclerViewItemClickListener: Reminder_RecyclerView_ItemClickListener? = null

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

        val time = "${reminder.start}-${reminder.end}"

        holder.timings.text = time
        holder.title.text = reminder.event
    }

    fun setOnEventClickListener(reminderRecyclerViewItemClickListener: Reminder_RecyclerView_ItemClickListener) {
        this.reminderRecyclerViewItemClickListener = reminderRecyclerViewItemClickListener
    fun setOnEventClickListener(reminderRecyclerviewItemClickListener: ReminderRecyclerViewItemClickListener) {
        this.reminderRecyclerViewItemClickListener = reminderRecyclerviewItemClickListener
    }

    inner class EventsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val row_layout = itemView.relativeLayout_reminder_listItem
        init {
            row_layout.setOnClickListener {
                reminderRecyclerViewItemClickListener!!.onEventClick(itemView, layoutPosition)
            }
        }
    }

    public override fun getItem(position: Int): ReminderEntity {
        return super.getItem(position)

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