package com.summer.scheduler.ui.main.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.intent.MainIntent
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class ToDoListAdapter(private val context: Context) :
    ListAdapter<ToDoEntity, ToDoListAdapter.ToDoItemHolder>(DIFF_CALLBACK) {

    private var mainViewModel: MainViewModel =
        ViewModelProvider(
            context as ViewModelStoreOwner,
            ViewModelFactory(context.applicationContext as Application)
        )
            .get(MainViewModel::class.java)

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

        if (toDo.check) {
            holder.title.alpha = 0.35F
        } else {
            holder.title.alpha = 1.0F
        }

        holder.checkBox.setOnClickListener {
            toDo.check = !toDo.check
            if (toDo.check) {
                holder.title.alpha = 0.35F
            } else {
                holder.title.alpha = 1.0F
            }
            mainViewModel.viewModelScope.launch {
                mainViewModel.userIntent.send(MainIntent.UpdateToDo(toDo))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ToDoEntity> =
            object : DiffUtil.ItemCallback<ToDoEntity>() {

                override fun areItemsTheSame(oldItem: ToDoEntity, newItem: ToDoEntity): Boolean {
                    return oldItem.key == newItem.key
                }

                override fun areContentsTheSame(oldItem: ToDoEntity, newItem: ToDoEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }

}