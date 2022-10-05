package com.example.smartparking.data.provider.bubble

import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import java.util.ArrayList

interface BubbleInterface {
    var listBubbles: ArrayList<BubbleListModel>

    fun getMyBubbles(): ArrayList<BubbleListModel>
}