package com.example.smartparking.ui.parking.navigation.choice

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel;
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.network.FirestoreService
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class NavigationChoiceViewModel : ViewModel(){

    private var _rooms: MutableLiveData<ArrayList<RoomDetails>> = MutableLiveData<ArrayList<RoomDetails>>() // add arrayListOf() inside ()??

    init {

            val firestoreService = FirestoreService()
            val databaseNetworkDataSource = DatabaseNetworkDataSourceImpl(firestoreService)
            databaseNetworkDataSource.downloadedLocations.observeForever(Observer {
                _rooms.value = it
            })
            databaseNetworkDataSource.fetchLocations()
    }

    internal fun getSelectedLocation(index : Int): RoomDetails{
        if (_rooms.value?.get(index) != null ){
            return _rooms.value?.get(index) ?: RoomDetails()
        }
        return RoomDetails()
    }

    internal var rooms : MutableLiveData<ArrayList<RoomDetails>>
        get() {return _rooms}
        set(value) {_rooms = value}

}
