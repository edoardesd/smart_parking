package com.example.smartparking.data.repository

import androidx.lifecycle.LiveData
import com.example.smartparking.data.db.RoomDetails

interface LocationRepository {

    suspend fun getLocationList(): LiveData<RoomDetails>
}