package com.example.smartparking.ui.parking.control

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.network.MQTTConnectionParams
import com.example.smartparking.data.network.MQTTManager
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.io.UnsupportedEncodingException
import java.util.*

const val serverURI = "tcp://broker.hivemq.com:1883"
const val topic = "smart/parking/polimi"

class ControlViewModel(app: Application) : AndroidViewModel(app){

    private var _bitmap: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    private val context = getApplication<Application>().applicationContext
    private val uniqueID : String = "parking" + UUID.randomUUID().toString()
    private val connectionParams = MQTTConnectionParams(uniqueID, serverURI, topic,2)
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
                    decodeMessage(mqttMessage)

            }
            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
            }
        })
    }

    fun subscribe(topic: String, qos: Int = 1){
        try {
            mqttManager.getMQTTClient().subscribe(topic, qos, null, object:IMqttActionListener {
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

        fun decodeMessage(message: MqttMessage?) {
        val decodedImageBytes: ByteArray
        try {
            decodedImageBytes = Base64.decode(message.toString().toByteArray(), Base64.DEFAULT)
            } catch (e: UnsupportedEncodingException) {
                Log.d(TAG, "This is not a base64 data", e)
                return
            } catch (e: IllegalArgumentException) {
                Log.d(TAG, "This is not a base64 data", e)
                return
            }

            Log.d(TAG, "Image decoded.")
            val tmpBitmap : Bitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.size)
            _bitmap.value = tmpBitmap
            Log.d(TAG, "Image passed to view.")
    }

    internal var bitmap : MutableLiveData<Bitmap>
        get() {return _bitmap}
        set(value) {_bitmap = value}
}