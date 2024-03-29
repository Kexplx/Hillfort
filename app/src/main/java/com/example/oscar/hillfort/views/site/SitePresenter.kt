package com.example.oscar.hillfort.views.site

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import com.example.oscar.hillfort.helpers.*
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
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
            locationService.removeLocationUpdates(locationCallback)
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
        async(UI) {
            if (edit) {
                app.sites.update(site)
            } else {
                app.sites.create(site)
            }
            view?.finish()
        }
    }

    fun doDelete() {
        async(UI) {
            app.sites.delete(site)
            view?.finish()
        }
    }

    fun doSelectImageFromCamera(imageNumber: Int) {
        if (checkCameraPermissions(view!!)) {
            dispatchTakePictureIntent(view!!, imageNumber)
        }
    }

    fun doSelectImageFromGallery(imageNumber: Int) {
        showImagePicker(view!!, imageNumber)
    }

    private fun saveBitmap(bitmap: Bitmap?, file: File) {
        if (bitmap != null) {
            try {
                var outputStream: FileOutputStream? = null
                try {
                    outputStream = FileOutputStream(file)
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        outputStream
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        outputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        var path: File? = null
        if (data.data == null && requestCode != LOCATION_REQUEST) {
            val bm = data.extras.get("data") as Bitmap
            path = createImageFile(view!!)
            saveBitmap(bm, path)
        }
        when (requestCode) {
            IMAGE_0_REQUEST -> {
                site.images[0] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(0, site.images[0])
            }
            IMAGE_1_REQUEST -> {
                site.images[1] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(1, site.images[1])
            }
            IMAGE_2_REQUEST -> {
                site.images[2] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(2, site.images[2])
            }
            IMAGE_3_REQUEST -> {
                site.images[3] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(3, site.images[3])
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
