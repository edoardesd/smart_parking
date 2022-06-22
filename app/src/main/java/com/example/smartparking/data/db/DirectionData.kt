package com.example.smartparking.data.db

data class DirectionData(
    var origins: String = "",
    var destinations: String = "",
    var sensor: String = "false",
    var units: String = "metrics",
    var mode: String = "")
