package com.example.smartparking.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Entity(tableName = "Rooms")
@Parcelize
data class RoomDetails(
    val name : String = "",
    val building : Int = 0,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var id : String = name

    override fun toString(): String {
        return "$name"
    }
}

