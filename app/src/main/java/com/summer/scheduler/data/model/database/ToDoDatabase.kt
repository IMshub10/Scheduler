package com.summer.scheduler.data.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.summer.scheduler.data.model.dao.ToDoDao
import com.summer.scheduler.data.model.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun getToDoDao(): ToDoDao

    companion object {
        private var instance: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            val tempInst = instance
            if (tempInst != null) {
                return tempInst
            }
            synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "to_do_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                instance = inst
                return inst
            }
        }
    }
}