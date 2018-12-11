package com.example.oscar.hillfort.views.map

import com.example.oscar.hillfort.main.MainApp
import com.example.oscar.hillfort.models.SiteModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SiteMapPresenter (var view : SiteMapsView) {

    var app : MainApp = view.application as MainApp
    lateinit var map : GoogleMap

    fun getSiteByTag(tag : Long) : SiteModel {
        return app.sites.findById(tag)!!
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        app.sites.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
        }

        if (app.sites.findAll().isNotEmpty()) {
            var lastAddedSite = app.sites.findAll().last()
            var loc = LatLng(lastAddedSite.lat, lastAddedSite.lng)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, lastAddedSite.zoom))
        }

        map.setOnMarkerClickListener(view)
    }
}