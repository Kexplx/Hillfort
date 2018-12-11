package com.example.oscar.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SiteModel (var id: Long = 0,
                      var title: String = "",
                      var description: String = "",
                      var notes : String = "",
                      var images : MutableList<String> = mutableListOf("", "", "", ""),
                      var lat : Double = 0.0,
                      var lng: Double = 0.0,
                      var zoom: Float = 0f,
                      var dateVisited : String = "",
                      var hasBeenVisited : Boolean = false) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable