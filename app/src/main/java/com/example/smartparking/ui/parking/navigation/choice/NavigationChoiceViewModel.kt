package com.example.smartparking.ui.parking.navigation.choice

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel;
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreeBonardi
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreePonzio
import com.example.smartparking.data.network.*
import com.example.smartparking.data.network.MQTTMessage.MQTTMessage
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import com.example.smartparking.data.recycleList.BubbleProvider
import com.example.smartparking.data.recycleList.LessonProvider
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class NavigationChoiceViewModel(app: Application) : AndroidViewModel(app) {

    private var _rooms: MutableLiveData<ArrayList<RoomDetails>> =
        MutableLiveData<ArrayList<RoomDetails>>() // add arrayListOf() inside ()??
    private var _lessons: MutableLiveData<ArrayList<LessonListModel>> =
        MutableLiveData<ArrayList<LessonListModel>>()
    private var _bubbles: MutableLiveData<ArrayList<BubbleListModel>> =
        MutableLiveData<ArrayList<BubbleListModel>>()
    private var _bubblesSelected: MutableLiveData<ArrayList<BubbleListModel>> =
        MutableLiveData<ArrayList<BubbleListModel>>()

    private val context = getApplication<Application>().applicationContext
    private val uniqueID: String = "parking" + UUID.randomUUID().toString()
    private var currentTopics = arrayOf("$BASE_TOPIC+/prediction")
    private val connectionParams = MQTTConnectionParams(
        uniqueID, SERVER_URI_MQTT,
        currentTopics,
        intArrayOf(2)
    )

    private val mqttManager: MQTTManager = MQTTManager(connectionParams, context)

    init {

//        val firestoreService = FirestoreService()
//        val databaseNetworkDataSource = DatabaseNetworkDataSourceImpl(firestoreService)
//        databaseNetworkDataSource.downloadedLocations.observeForever(Observer {
//            _rooms.value = it
//        })
//        databaseNetworkDataSource.fetchLocations()
        mqttManager.connect()
        mqttManager.getMQTTClient().setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.d(ContentValues.TAG, "Connection completed $s")
                mqttManager.subscribe(connectionParams.topic, connectionParams.qos)

            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(ContentValues.TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.d(ContentValues.TAG, "Received message from topic: $topic")

                selectMessageToParse(topic, mqttMessage)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
            }
        })

        val internalLesson = LessonProvider()
        var internalBubbles = BubbleProvider()
        _lessons.value = internalLesson.getAllLessonsLocal()
        _bubbles.value = internalBubbles.getAllBubblesLocal()

    }

    private fun selectMessageToParse(topic: String, mqttMessage: MqttMessage) {
//        val messageJson = Gson().fromJson(mqttMessage.toString(), MQTTMessage::class.java)

        if (topic.contains(PREDICTION_TAG)) {
            with(topic) {
                when {
                    contains("deib") -> globalParkingFreePonzio = mqttMessage.toString().toInt()
                    contains("dastu") -> globalParkingFreeBonardi = mqttMessage.toString().toInt()
                }
                Log.d(
                    TAG,
                    "message: ${
                        mqttMessage.toString().toInt()
                    } ponzio $globalParkingFreePonzio, bonardi $globalParkingFreeBonardi"
                )
            }
        }

    }


//    internal fun getSelectedLocation(index: Int): RoomDetails {
//        if (_rooms.value?.get(index) != null) {
//            return _rooms.value?.get(index) ?: RoomDetails()
//        }
//        return RoomDetails()
//    }

    internal var rooms: MutableLiveData<ArrayList<RoomDetails>>
        get() {
            return _rooms
        }
        set(value) {
            _rooms = value
        }

    internal var lessons: MutableLiveData<ArrayList<LessonListModel>>
        get() {
            return _lessons
        }
        set(value) {
            _lessons = value
        }

    internal var bubbles: MutableLiveData<ArrayList<BubbleListModel>>
        get() {
            return _bubbles
        }
        set(value) {
            _bubbles = value
        }

    internal var bubblesSelected: MutableLiveData<ArrayList<BubbleListModel>>
        get() {
            return _bubblesSelected
        }
        set(value) {
            _bubblesSelected = value
        }

}
