package com.example.smartparking.data.network.choice

import androidx.lifecycle.LiveData
import com.example.smartparking.data.db.RoomDetails

interface DatabaseNetworkDataSource {
    val downloadedLocations: LiveData<ArrayList<RoomDetails>>

    fun fetchLocations()
}