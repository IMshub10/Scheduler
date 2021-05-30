package com.summer.scheduler.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ToDoEntity

class ToDoListAdapter(private val context: Context) :
    ListAdapter<ToDoEntity, ToDoListAdapter.ToDoItemHolder>(DIFF_CALLBACK) {
    class ToDoItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox_toDoCheck)
        val title: TextView = itemView.findViewById(R.id.textView_toDoText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.to_do_list_item, parent, false)
        return ToDoItemHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoItemHolder, position: Int) {
        val toDo = getItem(position)
        holder.checkBox.isChecked = toDo.check
        holder.title.text = toDo.title
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ToDoEntity> =
            object : DiffUtil.ItemCallback<ToDoEntity>() {

                override fun areItemsTheSame(oldItem: ToDoEntity, newItem: ToDoEntity): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areContentsTheSame(oldItem: ToDoEntity, newItem: ToDoEntity): Boolean {
                    TODO("Not yet implemented")
                }
            }
    }

}