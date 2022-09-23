package com.example.smartparking.ui.parking.navigation.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartparking.data.TripDetails

class NavigationTripViewModel(app: Application) : AndroidViewModel(app) {

    private var _tripDetails : MutableLiveData<TripDetails> = MutableLiveData<TripDetails>()



    internal var tripDetails : MutableLiveData<TripDetails>
        get() {return  _tripDetails}
        set(value) {_tripDetails = value}
}
