package com.example.smartparking.ui.parking.control

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalDestinationInfo
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.data.network.*
import com.example.smartparking.data.network.MQTTMessage.MQTTMessage
import com.example.smartparking.internal.ParkingLots
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.*
import java.io.UnsupportedEncodingException
import java.util.*

//
//const val serverURI = "tcp://broker.hivemq.com:1883"
//const val TOPIC_IMAGE = "smart/parking/polimi/+/image"
//const val TOPIC_PREDICTION = "smart/parking/polimi/+/prediction"
//const val BASE_TOPIC = "smart/parking/polimi/"

const val STARTUP_PARKING = "deib"
//const val IMAGE_TAG = "image"
//const val PREDICTION_TAG = "prediction"


class ControlViewModel(app: Application) : AndroidViewModel(app) {

    private var _bitmap: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    private var _bitmapPonzio: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    private lateinit var bitPonzio: Bitmap
    private lateinit var bitBonardi: Bitmap
    private var _slotsPrediction: MutableLiveData<Int> = MutableLiveData<Int>()
    private val context = getApplication<Application>().applicationContext
    private val uniqueID: String = "parking" + UUID.randomUUID().toString()
    private var currentTopics =
        arrayOf("$BASE_TOPIC+/image", "$BASE_TOPIC+/prediction")
    private val connectionParams = MQTTConnectionParams(
        uniqueID, SERVER_URI_MQTT,
        currentTopics,
        intArrayOf(2, 2)
    )
    private val mqttManager: MQTTManager = MQTTManager(connectionParams, context)
    private lateinit var selectedParking: String
//    private lateinit var oldSelectedParking: String
    private lateinit var destinationInfo: InfoText

    init {
        if (SmartParkingApplication.isDestinationInitialized()) {
            initializePark()
//            currentTopics = arrayOf("$BASE_TOPIC$selectedParking/image", "$BASE_TOPIC$selectedParking/prediction")
        }
        mqttManager.connect()
        mqttManager.getMQTTClient().setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.d(TAG, "Connection completed $s")
                mqttManager.subscribe(connectionParams.topic, connectionParams.qos)

            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.d(TAG, "Received message from topic: $topic")

                selectMessageToParse(topic, mqttMessage)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
            }
        })
    }

    fun initializePark() {
        destinationInfo = globalDestinationInfo
        selectedParking = when (destinationInfo.infoTransportTime.parkingLot) {
            ParkingLots.BONARDI -> "dastu"
            ParkingLots.PONZIO -> "deib"
        }
        Log.d(TAG, "Selected parking $selectedParking")
    }

    private fun selectMessageToParse(topic: String, mqttMessage: MqttMessage) {
        if (topic.contains(IMAGE_TAG)) {
            if (topic.contains("deib")) {
                bitPonzio = decodeMessage(mqttMessage)
            }

            if (topic.contains("dastu")) {
                bitBonardi = decodeMessage(mqttMessage)
            }
            updateImage(destinationInfo.infoTransportTime.parkingLot)
        }

        if (topic.contains(PREDICTION_TAG)) {
            if (topic.contains(selectedParking)) {
                _slotsPrediction.value = mqttMessage.toString().toInt()
            }
        }
    }

    fun updateImage(parkingLots: ParkingLots) {
        when(parkingLots){
            ParkingLots.BONARDI -> if (::bitBonardi.isInitialized) {
                _bitmap.value = bitBonardi
            }
            ParkingLots.PONZIO ->  if (::bitPonzio.isInitialized) {
                _bitmap.value = bitPonzio
            }
        }
    }

    private fun decodeMessage(message: MqttMessage?): Bitmap {
        val messageJson = Gson().fromJson(message.toString(), MQTTMessage::class.java)
        val decodedImageBytes = Base64.decode(messageJson.image.toByteArray(), Base64.DEFAULT)
//        try {
//            decodedImageBytes = Base64.decode(messageJson.image.toByteArray(), Base64.DEFAULT)
//        } catch (e: UnsupportedEncodingException) {
//            Log.d(TAG, "This is not a base64 data", e)
//        } catch (e: IllegalArgumentException) {
//            Log.d(TAG, "This is not a base64 data", e)
//        }

        Log.d(TAG, "Image decoded.")
        val tmpBitmap: Bitmap =
            BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.size)

//        _bitmap.value = tmpBitmap
        return tmpBitmap
    }

//    fun getSelectedParking(parkingName: String) {
//        selectedParking = parkingName.lowercase()
//        mqttManager.unsubscribe(currentTopics)
//        currentTopics =
//            arrayOf("$BASE_TOPIC$selectedParking/image", "$BASE_TOPIC$selectedParking/prediction")
//        mqttManager.subscribe(currentTopics, intArrayOf(2, 2))
//    }

    internal var bitmap: MutableLiveData<Bitmap>
        get() {
            return _bitmap
        }
        set(value) {
            _bitmap = value
        }

    internal var bitmapPonzio: MutableLiveData<Bitmap>
        get() {
            return _bitmapPonzio
        }
        set(value) {
            _bitmapPonzio = value
        }

    internal var slotsPrediction: MutableLiveData<Int>
        get() {
            return _slotsPrediction
        }
        set(value) {
            _slotsPrediction = value
        }
}