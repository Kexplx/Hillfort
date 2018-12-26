package com.example.oscar.hillfort.views.navigation

import android.annotation.SuppressLint
import com.example.oscar.hillfort.helpers.checkLocationPermissions
import com.example.oscar.hillfort.helpers.createDefaultLocationRequest
import com.example.oscar.hillfort.helpers.isPermissionGranted
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
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class NavigationPresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null

    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var locationCallback = LocationCallback()

    lateinit var sites: List<SiteModel>

    init {
        async(UI) {
            sites = app.sites.findAll()
        }
        if (checkLocationPermissions(view))
            doSetCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            if (it != null) {
                locationUpdate(Location(it.latitude, it.longitude))
            } else {
                locationUpdate(Location(defaultLocation.lat, defaultLocation.lng))
            }
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
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
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), 15f))
    }
}