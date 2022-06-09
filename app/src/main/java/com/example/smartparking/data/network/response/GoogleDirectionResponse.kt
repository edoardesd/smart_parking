package com.example.smartparking.data.network.response


import com.google.gson.annotations.SerializedName

data class GoogleDirectionResponse(
    @SerializedName("destination_addresses")
    val destinationAddresses: List<String>,
    @SerializedName("origin_addresses")
    val originAddresses: List<String>,
    val rows: List<Row>,
    val status: String
)