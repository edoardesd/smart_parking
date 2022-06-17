package com.example.smartparking.data.network.result

import androidx.lifecycle.LiveData
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.network.response.GoogleDirectionResponse
import retrofit2.http.Query

interface NavigationNetworkDataSource {
    val downloadedNavigation: LiveData<GoogleDirectionResponse>

    suspend fun fetchedNavigation(
        directionData: DirectionData,
        navigationMode: String
    )
}