package com.example.smartparking.data.recycleList

import com.example.smartparking.R
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import java.util.ArrayList

class BubbleProvider {

    private var listBubbles = ArrayList<BubbleListModel>()

    fun getAllBubblesLocal(): ArrayList<BubbleListModel>{

        listBubbles.add(BubbleListModel("Bar", R.drawable.bar))
        listBubbles.add(BubbleListModel("Library", R.drawable.library))
        listBubbles.add(BubbleListModel("Microwaves", R.drawable.microwaves))
        listBubbles.add(BubbleListModel("Park", R.drawable.park))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets))

        return listBubbles
    }
}
