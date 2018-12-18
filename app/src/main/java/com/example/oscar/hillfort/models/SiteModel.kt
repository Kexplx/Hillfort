package com.example.oscar.hillfort.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class SiteModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId: String = "",
    var title: String = "",
    var description: String = "",
    var notes: String = "",
    var images: MutableList<String> = mutableListOf("", "", "", ""),
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var favorite: Boolean = false,
    var rating: Float = 0f,
    var zoom: Float = 0f,
    var dateVisited: String = "",
    var hasBeenVisited: Boolean = false
) : Parcelable

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable