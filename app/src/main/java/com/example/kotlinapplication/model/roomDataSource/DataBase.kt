package com.example.kotlinapplication.model.roomDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlertEntity::class, FavoriteEntity::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun alertDao(): AlertDao
    abstract fun favoriteDao(): FavoriteDao
    companion object {
        @Volatile
        private var instance: DataBase? = null

        fun getDatabase(context: Context): DataBase? {
            if (instance == null) {
                synchronized(DataBase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            DataBase::class.java, "Application_Database"
                        )
                            .build()
                    }
                }
            }
            return instance
        }
    }
}

