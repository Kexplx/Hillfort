package com.example.oscar.hillfort.views.navigation

import android.annotation.SuppressLint
import com.example.oscar.hillfort.helpers.checkLocationPermissions
import com.example.oscar.hillfort.helpers.createDefaultLocationRequest
import com.example.oscar.hillfort.models.Location
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.BaseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class NavigationPresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null

    var destinationSite: SiteModel? = null

    var firstZoomSet = false
    private var defaultLocation = Location(52.245696, -7.139102, 15f)
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private val locationRequest = createDefaultLocationRequest()
    private var locationCallback = LocationCallback()

    lateinit var sites: List<SiteModel>

    init {
        async(UI) {
            sites = app.sites.findAll()
        }
        if (checkLocationPermissions(view)) {
            firstZoomSet = false
            doRestartLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude, 15f))
                }
            }
        }
        locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
    }

    fun stopLocationUpdates() {
        locationService.removeLocationUpdates(locationCallback)
    }

    fun setNewDestination(marker: Marker) {
        async(UI) {
            destinationSite = marker.tag as SiteModel
        }
    }

    fun distanceFromInMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((lat2 - lat1))
        val dLng = Math.toRadians((lng2 - lng1))
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(
            Math.toRadians(lat2)
        ) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c)
    }

    fun locationUpdate(location: Location) {
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true

        sites.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map?.addMarker(options)?.tag = it
        }

        val options = MarkerOptions().title("My Location").position(LatLng(location.lat, location.lng))
        map?.addMarker(options)

        if (!firstZoomSet) {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), 15f))
            firstZoomSet = true
        }

        if (destinationSite != null) {
            val distance =
                distanceFromInMeters(destinationSite!!.lat, destinationSite!!.lng, location.lat, location.lng)
            view?.updateCardWithDestination(destinationSite!!, distance)
        }
    }
}