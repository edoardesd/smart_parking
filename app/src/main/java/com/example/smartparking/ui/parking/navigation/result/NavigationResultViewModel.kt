package com.example.smartparking.ui.parking.navigation.result

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.data.db.NavigationWay
import com.example.smartparking.data.network.ConnectivityInterceptorImpl
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.data.provider.LocationProvider
import com.example.smartparking.data.provider.LocationProviderImpl
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class NavigationResultViewModel(app: Application) : AndroidViewModel(app) {

    private var _gpsOrigin: MutableLiveData<String> = MutableLiveData<String>()
    private var _navigationDataAll: MutableLiveData<NavigationWay> =
        MutableLiveData<NavigationWay>()
    private var _fullText: MutableLiveData<String> = MutableLiveData<String>()
    private var navigationWay: NavigationWay = NavigationWay()
    private val context = getApplication<Application>().applicationContext
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        FusedLocationProviderClient(context)

    private var _infoTextCar: MutableLiveData<InfoText> = MutableLiveData<InfoText>()
    private var _infoTextBike: MutableLiveData<InfoText> = MutableLiveData<InfoText>()
    private var _textCar: InfoText = InfoText()
    private var _textBike: InfoText = InfoText()

    private val apiService = GoogleAPIService(ConnectivityInterceptorImpl(context))
    private val navigationNetworkDataSourceCar = NavigationNetworkDataSourceImpl(apiService)
    private val navigationNetworkDataSourceBike = NavigationNetworkDataSourceImpl(apiService)
    private val locationProvider: LocationProvider =
        LocationProviderImpl(fusedLocationProviderClient, context)


    init {
        navigationNetworkDataSourceBike.downloadedNavigation.observeForever(Observer {
            navigationWay.bicycling = it?.rows?.first()?.elements?.first()?.duration?.value?.seconds
            _navigationDataAll.value = navigationWay
            _textBike.infoTransportTime?.transportTime = navigationWay.bicycling!!

            _infoTextBike.value = _textBike
        })

        navigationNetworkDataSourceCar.downloadedNavigation.observeForever(Observer {
//            Log.d(TAG, "Car $it")
            navigationWay.driving = it?.rows?.first()?.elements?.first()?.duration?.value?.seconds
            _navigationDataAll.value = navigationWay
            _textCar.infoTransportTime?.transportTime = navigationWay.driving!!
            _infoTextCar.value = _textCar
            Log.d(TAG, "car ${navigationWay.driving}")

        })
    }

//    fun getTextData(directionData: DirectionData){
//        infoTextCar.infoTransportTime.setWalkTime(directionData.destinations)
//    }

    fun getNavigationData(
        requestDirectionDataCar: DirectionData,
        requestDirectionDataBike: DirectionData,
        navigation: NavigationDetails
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            _gpsOrigin.value = locationProvider.getPreferredLocation()
            requestDirectionDataCar.origins = _gpsOrigin.value!!
            requestDirectionDataBike.origins = _gpsOrigin.value!!
            Log.d(TAG, "Current Location: ${_gpsOrigin.value!!}")
            navigationNetworkDataSourceBike.fetchedNavigation(
                requestDirectionDataBike,
                TransportMode.BICYCLING.name.lowercase()
            )
            navigationNetworkDataSourceCar.fetchedNavigation(
                requestDirectionDataCar,
                TransportMode.DRIVING.name.lowercase()
            )

            getTextDataBike(navigation)
            getTextDataCar(navigation)
        }
    }

    private fun getTextDataCar(navigation: NavigationDetails) {
        val navigationLesson = navigation.lesson as LessonListModel
        _textCar.infoTransportTime?.parkingLot = navigationLesson.parkingPlace
        _textCar.infoTransportTime?.transportMode = TransportMode.DRIVING
//        _fullText.value = _textCar.fullText()
        _infoTextCar.value = _textCar

        Log.d(TAG, "value here ${_infoTextCar.value?.infoTransportTime?.transportTime}")

        _textBike.infoTransportTime?.parkingLot = navigationLesson.parkingPlace
        _textBike.infoTransportTime?.transportMode = TransportMode.BICYCLING

        _textBike.infoTransportTime?.setParkTime()
//        _fullText.value = _textBike.fullText()
        _infoTextBike.value = _textBike
    }

    private fun getTextDataBike(navigation: NavigationDetails) {


//        Log.d(TAG, "value bike ${_infoTextBike.value?.infoTransportTime?.transportTime}")
//        Log.d(TAG, "full bike ${_fullText.value}")

    }


    fun getLocationString(): String {
        return if (!locationProvider.isGpsPosition()) {
            _gpsOrigin.value!!
        } else "Current Location"
    }

    internal var gpsOrigin: MutableLiveData<String>
        get() {
            return _gpsOrigin
        }
        set(value) {
            _gpsOrigin = value
        }

    internal var navigationDataAll: MutableLiveData<NavigationWay>
        get() {
            return _navigationDataAll
        }
        set(value) {
            _navigationDataAll = value
        }

//    internal var fullText : MutableLiveData<String>
//        get() {return _fullText}
//        set(value) {_fullText = value}

    internal var infoTextCar: MutableLiveData<InfoText>
        get() {
            return _infoTextCar
        }
        set(value) {
            _infoTextCar = value
        }

    internal var infoTextBike: MutableLiveData<InfoText>
        get() {
            return _infoTextBike
        }
        set(value) {
            _infoTextBike = value
        }
}