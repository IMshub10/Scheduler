package com.summer.scheduler.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_table")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val key: String,
    val title: String,
    val day: Int,
    val check: Boolean
)