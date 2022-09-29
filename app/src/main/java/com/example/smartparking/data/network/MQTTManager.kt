package com.example.smartparking.data.network

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import info.mqtt.android.service.MqttAndroidClient
//import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTManager(private val connectionParams: MQTTConnectionParams, val context: Context) {

    private var mqttClient = MqttAndroidClient(context, connectionParams.host, connectionParams.clientId)

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

    fun subscribe(topic: Array<String>, qos: IntArray){
        try {
            mqttClient.subscribe(topic, qos, null, object:IMqttActionListener {
                override fun onSuccess(asyncActionToken:IMqttToken) {
                    topic.forEach { tp -> Log.d(TAG, "Subscribed to $tp")} }
                override fun onFailure(asyncActionToken:IMqttToken, exception:Throwable) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }
            })
        }
        catch (e:MqttException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic: Array<String>){
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

    fun getMQTTClient() : MqttAndroidClient {
        return mqttClient
    }

}

data class MQTTConnectionParams(val clientId:String, val host: String, val topic: Array<String>, val qos: IntArray){

}