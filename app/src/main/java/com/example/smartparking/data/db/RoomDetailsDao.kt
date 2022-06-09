package com.example.smartparking.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(roomDetails: RoomDetails)

    @Query("select * from Rooms")
    fun getRoomDetails(): LiveData<RoomDetails>
}