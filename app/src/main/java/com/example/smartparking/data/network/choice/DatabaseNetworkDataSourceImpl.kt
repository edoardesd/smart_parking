package com.example.smartparking.data.network.choice

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.network.FirestoreService
import com.example.smartparking.internal.NoConnectivityException

class DatabaseNetworkDataSourceImpl(
    private val firestoreService: ArrayList<RoomDetails>
) : DatabaseNetworkDataSource {
    private val _downloadedLocations: MutableLiveData<ArrayList<RoomDetails>> = MutableLiveData<ArrayList<RoomDetails>>()
    override val downloadedLocations: LiveData<ArrayList<RoomDetails>>
        get() = _downloadedLocations

     override fun fetchLocations() {
        try {
            Log.d(TAG, "fetching Locations")
            val fetchedLocations = firestoreService
            _downloadedLocations.postValue(fetchedLocations)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}