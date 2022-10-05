package com.example.smartparking.data.provider.lesson

import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalUserType
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class LessonProvider {

//    private var lesson = ArrayList<LessonListModel>()
//    private var loadedImg: Int = R.drawable.bonardi_student
    private var studentArchitectureLesson = StudentArchitectureLesson()
    private var studentEngineeringLesson = StudentEngineeringLesson()
    private var profArchitectureLesson = ProfArchitectureLesson()
    private var profEngineeringLesson = ProfEngineeringLesson()
    private var guestLesson = GuestLesson()

    fun getAllLessonsLocal(): ArrayList<LessonListModel> {
        return when (globalUserType) {
            UserType.STUDENT_ARCHITECTURE -> studentArchitectureLesson.getMyLessons()
            UserType.STUDENT_ENGINEERING -> studentEngineeringLesson.getMyLessons()
            UserType.PROF_ARCHITECTURE -> profArchitectureLesson.getMyLessons()
            UserType.PROF_ENGINEERING -> profEngineeringLesson.getMyLessons()
            UserType.GUEST -> guestLesson.getMyLessons()
        }
    }
}