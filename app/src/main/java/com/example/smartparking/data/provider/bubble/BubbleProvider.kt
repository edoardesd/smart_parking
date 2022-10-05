package com.example.smartparking.data.provider.bubble

import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.data.provider.lesson.*
import com.example.smartparking.internal.CrowdLevel
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import java.util.ArrayList
import kotlin.time.Duration.Companion.minutes

class BubbleProvider {

    private var studentArchitectureBubble = StudentArchitectureBubble()
    private var studentEngineeringBubble = StudentEngineeringBubble()
    private var profArchitectureBubble = ProfArchitectureBubble()
    private var profEngineeringBubble = ProfEngineeringBubble()
    private var guestBubble = GuestBubble()

    fun getAllBubblesLocal(): ArrayList<BubbleListModel>{
        return when (SmartParkingApplication.globalUserType) {
            UserType.STUDENT_ARCHITECTURE -> studentArchitectureBubble.getMyBubbles()
            UserType.STUDENT_ENGINEERING -> studentEngineeringBubble.getMyBubbles()
            UserType.PROF_ARCHITECTURE -> profArchitectureBubble.getMyBubbles()
            UserType.PROF_ENGINEERING -> profEngineeringBubble.getMyBubbles()
            UserType.GUEST -> guestBubble.getMyBubbles()
        }
    }
}
