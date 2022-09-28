package com.example.smartparking.data.recycleList

import com.example.smartparking.R
import com.example.smartparking.internal.CrowdLevel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import java.util.ArrayList
import kotlin.time.Duration.Companion.minutes

class BubbleProvider {

    private var listBubbles = ArrayList<BubbleListModel>()

    fun getAllBubblesLocal(): ArrayList<BubbleListModel>{

        listBubbles.add(BubbleListModel("Bar", R.drawable.bar, CrowdLevel.values().random(), 4.minutes))
        listBubbles.add(BubbleListModel("Library", R.drawable.library, CrowdLevel.values().random(), 8.minutes))
        listBubbles.add(BubbleListModel("Microwaves", R.drawable.microwaves, CrowdLevel.values().random(), 2.minutes))
        listBubbles.add(BubbleListModel("Park", R.drawable.park, CrowdLevel.values().random(), 8.minutes))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room, CrowdLevel.values().random(), 9.minutes))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets, CrowdLevel.values().random(), 3.minutes))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room, CrowdLevel.values().random(), 1.minutes))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets, CrowdLevel.values().random(), 2.minutes))

        return listBubbles
    }
}
