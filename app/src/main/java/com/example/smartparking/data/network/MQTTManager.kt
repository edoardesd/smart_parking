package com.example.smartparking.data.network

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class MQTTManager(val connectionParams: MQTTConnectionParams, val context: Context) {

    private var mqttClient = MqttAndroidClient(context, connectionParams.host,connectionParams.clientId + id(context))
    private var uniqueID : String? = null
    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    init {
        mqttClient.setCallback(object: MqttCallbackExtended {
            override fun connectComplete(b:Boolean, s:String) {
                Log.d(TAG, "Connection completed $s")
            }
            override fun connectionLost(cause : Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }
            override fun messageArrived(topic:String, mqttMessage: MqttMessage) {
                Log.d(TAG, "Received message from topic: $topic")
//                handleMessage(mqttMessage)

            }
            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
            }
        })
    }

    fun connect(){
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        var params = this.connectionParams
        try {
            mqttClient.connect(mqttConnectOptions, null, object: IMqttActionListener {
                override fun onSuccess(asyncActionToken:IMqttToken) {
                    Log.d(TAG, "Connection success")
                    subscribe(params.topic, params.qos)

                }
                override fun onFailure(asyncActionToken:IMqttToken, exception:Throwable) {
                    Log.d(TAG, "Connection failure")
                }
            })
        }
        catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(){
        try {
            mqttClient.disconnect(null,object :IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }

            })
        }
        catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    // Subscribe to topic
    fun subscribe(topic: String, qos: Int = 1){
        try {
            mqttClient.subscribe(topic, qos, null, object:IMqttActionListener {
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

    // Unsubscribe the topic
    fun unsubscribe(topic: String){
        try {
            mqttClient.unsubscribe(topic,null,object :IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        }
        catch (e:MqttException) {
            System.err.println("Exception unsubscribe")
            e.printStackTrace()
        }

    }

    @Synchronized fun id(context:Context):String {
        if (uniqueID == null) {
            val sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE)
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null)
            {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
        }
        return uniqueID!!
    }

}

data class MQTTConnectionParams(val clientId:String, val host: String, val topic: String, val qos: Int, val username: String, val password: String){

}