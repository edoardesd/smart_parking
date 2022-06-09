package com.example.smartparking.data.network

import androidx.lifecycle.LiveData
import com.example.smartparking.data.network.response.GoogleDirectionResponse
import retrofit2.http.Query

interface NavigationNetworkDataSource {
    val downloadedNavigation: LiveData<GoogleDirectionResponse>

    suspend fun fetchedNavigation(
        origins: String,
        destinations: String,
        sensor: String,
        units: String,
        mode: String
    )
}