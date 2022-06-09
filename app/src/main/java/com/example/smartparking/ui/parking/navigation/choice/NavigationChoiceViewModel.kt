package com.example.smartparking.ui.parking.navigation.choice

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.smartparking.data.db.FirestormRoom
//import com.example.smartparking.data.db.FirestormRoom.Companion.toRoom
import com.example.smartparking.data.db.RoomDetails
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source

class NavigationChoiceViewModel : ViewModel(){

    private var _rooms: MutableLiveData<ArrayList<RoomDetails>> = MutableLiveData<ArrayList<RoomDetails>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToRooms()
    }

    private fun listenToRooms() {
        Log.d(TAG, "Listen all rooms")
        val roomsArrayList : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()

        firestore.collection("rooms")
            .addSnapshotListener { result, e ->
                Log.d(TAG, result.toString())
                if (result != null) {
                    for (document in result.documents) {
                        Log.d(TAG, document.toString())

                        val room = RoomDetails(
                            document.data?.getValue("room").toString(),
                            document.data?.getValue("building").toString().toInt(),
                            document.getGeoPoint("location")!!.latitude,
                            document.getGeoPoint("location")!!.longitude)

                        roomsArrayList.add(room)
                    }
                }

            }

    _rooms.value = roomsArrayList

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
