package com.summer.scheduler.ui.main.`interface`

import android.view.View

interface ReminderRecyclerViewItemClickListener {
    fun onEventClick(itemView: View, layoutPosition: Int)
}