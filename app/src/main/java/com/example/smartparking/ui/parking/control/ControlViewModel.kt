package com.example.smartparking.ui.parking.control

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.network.MQTTConnectionParams
import com.example.smartparking.data.network.MQTTManager
import com.example.smartparking.data.network.MQTTMessage.MQTTMessage
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.*
import java.io.UnsupportedEncodingException
import java.util.*

const val serverURI = "tcp://broker.hivemq.com:1883"
const val TOPIC_IMAGE = "smart/parking/polimi/deib/image"
const val TOPIC_PREDICTION = "smart/parking/polimi/deib/prediction"
const val TOPIC_IMAGE_DASTU = "smart/parking/polimi/dastu/image"
const val TOPIC_PREDICTION_DASTU = "smart/parking/polimi/dastu/prediction"


class ControlViewModel(app: Application) : AndroidViewModel(app){

    private var _bitmapDEIB: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    private var _bitmapDASTU: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    private var _slotsPrediction: MutableLiveData<Int> = MutableLiveData<Int>()
    private val context = getApplication<Application>().applicationContext
    private val uniqueID : String = "parking" + UUID.randomUUID().toString()
    private val connectionParams = MQTTConnectionParams(uniqueID, serverURI, TOPIC_IMAGE,2)
    private val mqttManager: MQTTManager = MQTTManager(connectionParams, context)

    init {
        mqttManager.connect()
        mqttManager.getMQTTClient().setCallback(object: MqttCallbackExtended {
            override fun connectComplete(b:Boolean, s:String) {
                Log.d(TAG, "Connection completed $s")
                subscribe(connectionParams.topic, connectionParams.qos)

            }
            override fun connectionLost(cause : Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }
            override fun messageArrived(topic:String, mqttMessage: MqttMessage) {
                Log.d(TAG, "Received message from topic: $topic")
                if (topic == TOPIC_IMAGE) {
                    decodeMessage(mqttMessage, topic)
                }
                if (topic == TOPIC_PREDICTION){
                    _slotsPrediction.value = mqttMessage.toString().toInt()
                }

                if (topic == TOPIC_IMAGE_DASTU) {
                    decodeMessage(mqttMessage, topic)
                    Log.d(TAG, "image dastu")
                }
                if (topic == TOPIC_PREDICTION_DASTU){
//                    _slotsPrediction.value = mqttMessage.toString().toInt()
                    Log.d(TAG, "prediction dastu")
                }

            }
            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
            }
        })
    }

    fun subscribe(topic: String, qos: Int = 1){
        try {
            mqttManager.getMQTTClient().subscribe(arrayOf(topic, TOPIC_PREDICTION, TOPIC_IMAGE_DASTU, TOPIC_PREDICTION_DASTU), intArrayOf(qos, 2, 2, 2), null, object:IMqttActionListener {
                override fun onSuccess(asyncActionToken:IMqttToken) {
                    Log.d(TAG, "Subscribed to $topic")                }
                override fun onFailure(asyncActionToken:IMqttToken, exception:Throwable) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }
            })
        }
        catch (e:MqttException) {
            e.printStackTrace()
        }
    }

        fun decodeMessage(message: MqttMessage?, topic: String) {
            val messageJson = Gson().fromJson(message.toString(), MQTTMessage::class.java)
            val decodedImageBytes: ByteArray
            try {
                decodedImageBytes = Base64.decode(messageJson.image.toByteArray(), Base64.DEFAULT)
            } catch (e: UnsupportedEncodingException) {
                Log.d(TAG, "This is not a base64 data", e)
                return
            } catch (e: IllegalArgumentException) {
                Log.d(TAG, "This is not a base64 data", e)
                return
            }

            Log.d(TAG, "Image decoded.")
            val tmpBitmap : Bitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.size)

            if (topic == TOPIC_IMAGE) {
                _bitmapDEIB.value = tmpBitmap
                Log.d(TAG, "bitmap deib")
            }

            if (topic == TOPIC_IMAGE_DASTU) {
                _bitmapDASTU.value = tmpBitmap
                Log.d(TAG, "bitmap dastu")

            }

            Log.d(TAG, "Image passed to view.")
    }

    internal var bitmapDEIB : MutableLiveData<Bitmap>
        get() {return _bitmapDEIB}
        set(value) {_bitmapDEIB = value}

    internal var bitmapDASTU : MutableLiveData<Bitmap>
        get() {return _bitmapDASTU}
        set(value) {_bitmapDASTU = value}

    internal var slotsPrediction : MutableLiveData<Int>
        get() {return _slotsPrediction}
        set(value) {_slotsPrediction = value}
}