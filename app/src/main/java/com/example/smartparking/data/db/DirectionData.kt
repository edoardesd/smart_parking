package com.example.smartparking.data.db

data class DirectionData(
    var origins: String = "",
    var destinations: String = "",
    var sensor: String = "",
    var units: String = "false",
    var mode: String = "metrics")
