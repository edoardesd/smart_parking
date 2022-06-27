package com.example.smartparking.data.network.MQTTMessage


import com.google.gson.annotations.SerializedName

data class MQTTMessage(
    val image: String,
    @SerializedName("available_slots")
    val availableSlots: Int
)