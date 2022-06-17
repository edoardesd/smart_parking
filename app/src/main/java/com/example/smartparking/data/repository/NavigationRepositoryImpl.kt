//package com.example.smartparking.data.repository
//
//import androidx.lifecycle.LiveData
//import com.example.smartparking.data.db.DirectionData
//import com.example.smartparking.data.network.response.GoogleDirectionResponse
//import com.example.smartparking.data.network.result.NavigationNetworkDataSource
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class NavigationRepositoryImpl(
//    private val requestDirectionData: DirectionData,
//    private val navigationNetworkDataSource: NavigationNetworkDataSource
//) : NavigationRepository {
//
//    var directionData: GoogleDirectionResponse? = null
//
//    init {
//        navigationNetworkDataSource.downloadedNavigation.observeForever {
//            newNavigation -> directionData = newNavigation
//        }
//    }
//
//    override suspend fun getNavigation(
//        origins: String,
//        destinations: String,
//        sensor: String,
//        units: String,
//        mode: String
//    ): LiveData<GoogleDirectionResponse> {
//        return withContext(Dispatchers.IO){
//            return@withContext fetchCurrentDirection()
//        }
//    }
//
//    private suspend fun fetchCurrentDirection() {
//        navigationNetworkDataSource.fetchedNavigation(requestDirectionData.origins,
//            requestDirectionData.destinations,
//        requestDirectionData.sensor,
//        requestDirectionData.units,
//        requestDirectionData.mode)
//    }
//}