package com.example.smartparking.data.repository

import androidx.lifecycle.LiveData
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.db.RoomDetailsDao
import com.example.smartparking.data.network.result.NavigationNetworkDataSource

class LocationRepositoryImpl(
    private val roomDetailsDao: RoomDetailsDao,
    private val navigationNetworkDataSource: NavigationNetworkDataSource
) : LocationRepository {
    override suspend fun getLocationList(): LiveData<RoomDetails> {
        TODO("Not yet implemented")
    }
}