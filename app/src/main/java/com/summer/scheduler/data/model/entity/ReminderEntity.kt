package com.summer.scheduler.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val key: Int,
    val event: String,
    val agenda: String,
    val start: String,
    val end: String,
    val people : String,
    val link : String,
    val location : String,
    val day: String  //Format: dd/MM/yyyy
)