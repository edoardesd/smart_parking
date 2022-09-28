package com.example.smartparking.data.network.result

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.response.GoogleDirectionResponse
import com.example.smartparking.internal.NoConnectivityException

class NavigationNetworkDataSourceImpl(
    private val googleAPIService: GoogleAPIService?
) : NavigationNetworkDataSource {

    private val _downloadedNavigation = MutableLiveData<GoogleDirectionResponse>()
    override val downloadedNavigation: LiveData<GoogleDirectionResponse>
        get() = _downloadedNavigation

    override suspend fun fetchedNavigation(directionData: DirectionData, navigationMode: String) {
        try {
            Log.d(TAG, "${directionData}, $navigationMode")
            val fetchedNavigation = googleAPIService!!
                .getGoogleDirection(directionData.origins,
                                directionData.destinations,
                                directionData.sensor,
                                directionData.units,
                                navigationMode)
                .await()
            _downloadedNavigation.postValue(fetchedNavigation)
            Log.d(TAG, fetchedNavigation.toString())
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}