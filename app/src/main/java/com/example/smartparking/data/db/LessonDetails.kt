package com.example.smartparking.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(tableName = "Lessons")
@Parcelize
data class LessonDetails(
    val building: String = "",
    val image: String = "",
    val lesson: String = "",
    val parking: String = "",
    val professor: String = "",
    val room: String = "",
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var id : String = lesson

    override fun toString(): String {
        return "$lesson"
    }
}
