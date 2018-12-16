package com.example.oscar.hillfort.views.site

import android.annotation.SuppressLint
import android.content.Intent
import com.example.oscar.hillfort.helpers.checkLocationPermissions
import com.example.oscar.hillfort.helpers.createDefaultLocationRequest
import com.example.oscar.hillfort.helpers.isPermissionGranted
import com.example.oscar.hillfort.helpers.showImagePicker
import com.example.oscar.hillfort.models.Location
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.VIEW
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SitePresenter(view: SiteView) : BasePresenter(view) {

    val IMAGE_0_REQUEST = 10
    val IMAGE_1_REQUEST = 11
    val IMAGE_2_REQUEST = 12
    val IMAGE_3_REQUEST = 13

    val LOCATION_REQUEST = 2


    var site = SiteModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false
    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras.getParcelable<SiteModel>("site_edit")
            view.showSite(site)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(site.lat, site.lng, site.zoom))
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun locationUpdate(lat: Double, lng: Double) {
        site.lat = lat
        site.lng = lng
        site.zoom = 15f
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().title(site.title).position(LatLng(site.lat, site.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(site.lat, site.lng), site.zoom))
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(site.lat, site.lng)
    }

    fun doAddOrSave(
        title: String,
        description: String,
        notes: String,
        date: String,
        isChecked: Boolean,
        favorite: Boolean,
        rating: Float
    ) {
        site.title = title
        site.description = description
        site.hasBeenVisited = isChecked
        site.notes = notes
        site.favorite = favorite
        site.rating = rating
        site.dateVisited = date
        if (edit) {
            app.sites.update(site)
        } else {
            app.sites.create(site)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.sites.delete(site)
        view?.finish()
    }

    fun doSelectImage(imageNumber: Int) {
        showImagePicker(view!!, imageNumber)
    }

    override fun doActivityResult(requestCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_0_REQUEST -> {
                site.images[0] = data.data.toString(); view?.updateImage(0, site.images[0])
            }
            IMAGE_1_REQUEST -> {
                site.images[1] = data.data.toString(); view?.updateImage(1, site.images[1])
            }
            IMAGE_2_REQUEST -> {
                site.images[2] = data.data.toString(); view?.updateImage(2, site.images[2])
            }
            IMAGE_3_REQUEST -> {
                site.images[3] = data.data.toString(); view?.updateImage(3, site.images[3])
            }

            LOCATION_REQUEST -> {
                val location = data.extras.getParcelable<Location>("location")
                site.lat = location.lat
                site.lng = location.lng
                site.zoom = location.zoom
                locationUpdate(site.lat, site.lng)
            }
        }
    }
}
