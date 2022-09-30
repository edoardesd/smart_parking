package com.example.smartparking.ui.parking.navigation.choice

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel;
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.network.FirestoreService
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import com.example.smartparking.data.recycleList.BubbleProvider
import com.example.smartparking.data.recycleList.LessonProvider
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class NavigationChoiceViewModel : ViewModel(){

    private var _rooms: MutableLiveData<ArrayList<RoomDetails>> = MutableLiveData<ArrayList<RoomDetails>>() // add arrayListOf() inside ()??
    private var _lessons: MutableLiveData<ArrayList<LessonListModel>> = MutableLiveData<ArrayList<LessonListModel>>()
    private var _bubbles: MutableLiveData<ArrayList<BubbleListModel>> = MutableLiveData<ArrayList<BubbleListModel>>()
    private var _bubblesSelected: MutableLiveData<ArrayList<BubbleListModel>> = MutableLiveData<ArrayList<BubbleListModel>>()
    init {

            val firestoreService = FirestoreService()
            val databaseNetworkDataSource = DatabaseNetworkDataSourceImpl(firestoreService)
            databaseNetworkDataSource.downloadedLocations.observeForever(Observer {
                _rooms.value = it
            })
            databaseNetworkDataSource.fetchLocations()

        val internalLesson = LessonProvider()
        var internalBubbles = BubbleProvider()
        _lessons.value = internalLesson.getAllLessonsLocal()
        _bubbles.value = internalBubbles.getAllBubblesLocal()

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

    internal var lessons : MutableLiveData<ArrayList<LessonListModel>>
        get() {return _lessons}
        set(value) {_lessons = value}

    internal var bubbles : MutableLiveData<ArrayList<BubbleListModel>>
    get() {return _bubbles}
    set(value) {_bubbles = value}

    internal var bubblesSelected : MutableLiveData<ArrayList<BubbleListModel>>
        get() {return _bubblesSelected}
        set(value) {_bubblesSelected = value}

}
