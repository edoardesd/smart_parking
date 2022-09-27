package com.example.smartparking.internal

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@OptIn(ExperimentalTime::class)
val DEFAULT_BIKE_WALK_TIME : Duration = 2.minutes


@OptIn(ExperimentalTime::class)
val DEFAULT_BIKE_PARKING_TIME : Duration = 1.minutes


enum class TransportMode {
    DRIVING, BICYCLING, WALKING
}