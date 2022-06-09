package com.example.smartparking.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RoomDetails::class],
    version = 1
)
abstract class RoomsDatabase  : RoomDatabase(){
    abstract fun roomsDetailsDao(): RoomDetailsDao

    companion object {
        @Volatile private var instance : RoomsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RoomsDatabase::class.java, "rooms.db")
                .build()
    }
}