package com.example.smartparking.internal

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@OptIn(ExperimentalTime::class)
val DEFAULT_CAR_WALK_BONARDI_TIME : Duration = 4.minutes

@OptIn(ExperimentalTime::class)
val DEFAULT_CAR_WALK_PONZIO_TIME : Duration = 3.minutes


@OptIn(ExperimentalTime::class)
val DEFAULT_BIKE_WALKING_TIME : Duration = 1.minutes


enum class TransportMode {
    DRIVING, BICYCLING, WALKING
}