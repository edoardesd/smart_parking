package com.example.smartparking.ui.parking.navigation.choice.recyclers

import com.example.smartparking.internal.CrowdLevel

class BubbleListModel(val title:String, val icon:Int, val crowdLevel: CrowdLevel, var extraMinutes: kotlin.time.Duration, var isSelected: Boolean = false) {
}