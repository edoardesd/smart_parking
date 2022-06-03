package com.example.smartparking.data.response


import com.google.gson.annotations.SerializedName

data class Element(
    val distance: Distance,
    val duration: Duration,
    val status: String
)