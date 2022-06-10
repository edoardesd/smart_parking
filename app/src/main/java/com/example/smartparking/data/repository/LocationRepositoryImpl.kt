package com.example.smartparking.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.db.RoomDetailsDao
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSource
import com.example.smartparking.data.network.result.NavigationNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class LocationRepositoryImpl(
    private val roomDetailsDao: RoomDetailsDao,
    private val databaseNetworkDataSource: DatabaseNetworkDataSource
) : LocationRepository {

    init {
        databaseNetworkDataSource.downloadedLocations.observeForever { it
            //persist data
            it.forEach{
                    location -> persistFetchedLocations(location)
            }
        }
    }

    override suspend fun getLocationList(): LiveData<RoomDetails> {
       return withContext(Dispatchers.IO) {
           return@withContext roomDetailsDao.getRoomDetails()
       }
    }

    private fun persistFetchedLocations(fetchedLocation: RoomDetails){
        GlobalScope.launch(Dispatchers.IO) {
            roomDetailsDao.upsert(fetchedLocation)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun initLocationData(){
        if (isFetchLocationNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentLocations()
    }

    private suspend fun fetchCurrentLocations(){
        databaseNetworkDataSource.fetchLocations()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isFetchLocationNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}