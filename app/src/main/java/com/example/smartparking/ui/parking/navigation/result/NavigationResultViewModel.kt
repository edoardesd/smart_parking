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
import com.example.smartparking.data.network.response.GoogleDirectionResponse
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.data.provider.LocationProvider
import com.example.smartparking.data.provider.LocationProviderImpl
import com.example.smartparking.internal.TransportMode
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class NavigationResultViewModel(app: Application) : AndroidViewModel(app) {

    private var _gpsOrigin : MutableLiveData<String> = MutableLiveData<String>()
    private var _navigationDataAll : MutableLiveData<NavigationWay> = MutableLiveData<NavigationWay>()
    private var navigationWay : NavigationWay = NavigationWay()
    private val context = getApplication<Application>().applicationContext
    private val fusedLocationProviderClient: FusedLocationProviderClient = FusedLocationProviderClient(context)

    private val apiService = GoogleAPIService(ConnectivityInterceptorImpl(context))
    private val navigationNetworkDataSourceCar = NavigationNetworkDataSourceImpl(apiService)
    private val navigationNetworkDataSourceBike = NavigationNetworkDataSourceImpl(apiService)
    private val locationProvider : LocationProvider = LocationProviderImpl(fusedLocationProviderClient, context)


    init {

        navigationNetworkDataSourceCar.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Car $it")

            navigationWay.driving = it?.rows?.first()?.elements?.first()?.duration?.value?.seconds
            _navigationDataAll.value = navigationWay
        })

        navigationNetworkDataSourceBike.downloadedNavigation.observeForever(Observer {
            Log.d(TAG, "Bike $it")
            navigationWay.bicycling = it?.rows?.first()?.elements?.first()?.duration?.value?.seconds
            _navigationDataAll.value = navigationWay


        })
    }

    fun getNavigationData(requestDirectionDataCar: DirectionData, requestDirectionDataBike: DirectionData)  {
        GlobalScope.launch(Dispatchers.Main) {
            _gpsOrigin.value = locationProvider.getPreferredLocation()
            requestDirectionDataCar.origins = _gpsOrigin.value!!
            requestDirectionDataBike.origins = _gpsOrigin.value!!
            Log.d(TAG, "Current Location: ${_gpsOrigin.value!!}")
            navigationNetworkDataSourceCar.fetchedNavigation(requestDirectionDataCar,
                TransportMode.DRIVING.name.lowercase())
             navigationNetworkDataSourceBike.fetchedNavigation(requestDirectionDataBike,
                TransportMode.BICYCLING.name.lowercase())
        }
    }

    fun getLocationString(): String{
        return if (!locationProvider.isGpsPosition()) {
            _gpsOrigin.value!!
        } else "Current Location"
    }

    internal var gpsOrigin : MutableLiveData<String>
        get() {return _gpsOrigin}
        set(value) {_gpsOrigin = value}

    internal var navigationDataAll : MutableLiveData<NavigationWay>
        get() {return _navigationDataAll}
        set(value) {_navigationDataAll = value}
}