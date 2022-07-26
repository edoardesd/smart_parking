package com.example.smartparking.data.db

import kotlin.time.Duration

data class NavigationWay(
    var driving : Duration? = null,
    var bicycling : Duration? = null,
    var walking : Duration? = null
)
