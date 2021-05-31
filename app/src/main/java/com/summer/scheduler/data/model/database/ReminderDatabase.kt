package com.summer.scheduler.data.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.summer.scheduler.data.model.dao.ReminderDao
import com.summer.scheduler.data.model.entity.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao

    companion object {

        private var instance: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            val tempInst = instance
            if (tempInst != null) {
                return tempInst
            }
            synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "reminder_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                instance = inst
                return inst
            }
        }
    }

}