package com.example.smartparking.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.network.response.GoogleDirectionResponse
import com.example.smartparking.internal.NoConnectivityException

class NavigationNetworkDataSourceImpl(
    private val googleAPIService: GoogleAPIService
) : NavigationNetworkDataSource {

    private val _downloadedNavigation = MutableLiveData<GoogleDirectionResponse>()
    override val downloadedNavigation: LiveData<GoogleDirectionResponse>
        get() = _downloadedNavigation

    override suspend fun fetchedNavigation(origins: String,
                                           destinations: String,
                                           sensor: String,
                                           units: String,
                                           mode: String) {
        try {
            val fetchedNavigation = googleAPIService
                .getGoogleDirection(origins, destinations, sensor, units, mode)
                .await()
            _downloadedNavigation.postValue(fetchedNavigation)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}