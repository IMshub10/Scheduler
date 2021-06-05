package com.summer.scheduler.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_table")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val key: Int,
    val title: String,
    val day: String, //Format: dd/MM/yyyy
    var check: Boolean
)