package com.example.smartparking.ui.parking.navigation.result

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.db.NavigationWay
import com.example.smartparking.data.network.ConnectivityInterceptorImpl
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.result.NavigationNetworkDataSource
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.internal.UnitSystem
import com.example.smartparking.ui.base.ScopedFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class NavigationResultViewModel(app: Application) : AndroidViewModel(app) {
    private var _navigationDataCar : MutableLiveData<String> = MutableLiveData<String>()
    private var _navigationDataBike : MutableLiveData<String> = MutableLiveData<String>()
    private var _navigationDataAll : MutableLiveData<NavigationWay> = MutableLiveData<NavigationWay>()
    private var navigationWay : NavigationWay = NavigationWay()
    private val context = getApplication<Application>().applicationContext

    private val apiService = GoogleAPIService(ConnectivityInterceptorImpl(context))
    private val navigationNetworkDataSourceCar = NavigationNetworkDataSourceImpl(apiService)
    private val navigationNetworkDataSourceBike = NavigationNetworkDataSourceImpl(apiService)

    init {

        navigationNetworkDataSourceCar.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Car $it")
            navigationWay.driving = it?.rows?.first()?.elements?.first()?.duration?.text
            _navigationDataAll.value = navigationWay
        })

        navigationNetworkDataSourceBike.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Bike $it")
            navigationWay.bicycling =  it?.rows?.first()?.elements?.first()?.duration?.text
            _navigationDataAll.value = navigationWay


        })
    }

    fun getNavigationData(requestDirectionData: DirectionData)  {
        GlobalScope.launch(Dispatchers.Main) {
            async { navigationNetworkDataSourceCar.fetchedNavigation(requestDirectionData,
                TransportMode.DRIVING.name.lowercase()) }
            async { navigationNetworkDataSourceBike.fetchedNavigation(requestDirectionData,
                TransportMode.BICYCLING.name.lowercase()) }
        }
    }

//
//     fun getDataCar(requestDirectionData: DirectionData) {
//         GlobalScope.launch(Dispatchers.Main) {
//             navigationNetworkDataSourceCar.fetchedNavigation(requestDirectionData,
//                 TransportMode.DRIVING.name.lowercase())
//         }
//    }
//
//    fun getDataBicycle(requestDirectionData: DirectionData) {
//        GlobalScope.launch(Dispatchers.Main) {
//            navigationNetworkDataSourceBike.fetchedNavigation(requestDirectionData,
//                TransportMode.BICYCLING.name.lowercase())
//        }
//    }


    internal var navigationDataCar : MutableLiveData<String>
        get() {return _navigationDataCar}
        set(value) {_navigationDataCar = value}

    internal var navigationDataBike : MutableLiveData<String>
        get() {return _navigationDataBike}
        set(value) {_navigationDataBike = value}

    internal var navigationDataAll : MutableLiveData<NavigationWay>
        get() {return _navigationDataAll}
        set(value) {_navigationDataAll = value}
}