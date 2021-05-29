package com.summer.scheduler.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.summer.scheduler.R
import com.summer.scheduler.data.model.ToDoItem

class ToDoListAdapter(private val toDoItems: ArrayList<ToDoItem>): RecyclerView.Adapter<ToDoListAdapter.ToDoItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.to_do_list_item, parent, false)
        return ToDoItemHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoItemHolder, position: Int) {
        holder.checkBox.isChecked = toDoItems[position].check
        holder.title.text = toDoItems[position].toDo
    }

    override fun getItemCount(): Int {
       return toDoItems.size
    }

    class ToDoItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.to_do_checkBox)
        val title: TextView = itemView.findViewById(R.id.to_do_actionText)
    }

    fun addItems(items: ArrayList<ToDoItem>) {
        toDoItems.clear()
        toDoItems.addAll(items)
        notifyDataSetChanged()
    }
}