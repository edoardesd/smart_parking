package com.example.smartparking.ui.parking.navigation.result

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.network.ConnectivityInterceptorImpl
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.result.NavigationNetworkDataSource
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NavigationResultViewModel(app: Application) : AndroidViewModel(app) {
    private var _navigationDataCar : MutableLiveData<String> = MutableLiveData<String>()
    private var _navigationDataBike : MutableLiveData<String> = MutableLiveData<String>()
    private val context = getApplication<Application>().applicationContext

    private val apiService = GoogleAPIService(ConnectivityInterceptorImpl(context))
    private val navigationNetworkDataSourceCar = NavigationNetworkDataSourceImpl(apiService)
    private val navigationNetworkDataSourceBike = NavigationNetworkDataSourceImpl(apiService)

    init {

        navigationNetworkDataSourceCar.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Car $it")
            _navigationDataCar.value = it?.rows?.first()?.elements?.first()?.duration?.text
        })

        navigationNetworkDataSourceBike.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Bike $it")
            _navigationDataBike.value = it?.rows?.first()?.elements?.first()?.duration?.text
        })
////        navigationNetworkDataSourceCar.fetchedNavigation()
//
//
//        val navigationDataSourceCar = apiService?.let { NavigationNetworkDataSourceImpl(it) }
//        val navigationNetworkDataSourceBike = apiService?.let { NavigationNetworkDataSourceImpl(it) }
//
////        getDataCar()
////        getDataBicycle()
    }

     fun getDataCar(requestDirectionData: DirectionData) {
         requestDirectionData.mode = "driving"
     GlobalScope.launch(Dispatchers.Main) {
         navigationNetworkDataSourceCar.fetchedNavigation(requestDirectionData, "driving")
     }
    }

    fun getDataBicycle(requestDirectionData: DirectionData) {
        requestDirectionData.mode = "walking"
        GlobalScope.launch(Dispatchers.Main) {
            navigationNetworkDataSourceBike.fetchedNavigation(requestDirectionData, "walking")
        }
    }


    internal var navigationDataCar : MutableLiveData<String>
        get() {return _navigationDataCar}
        set(value) {_navigationDataCar = value}

    internal var navigationDataBike : MutableLiveData<String>
        get() {return _navigationDataBike}
        set(value) {_navigationDataBike = value}
}