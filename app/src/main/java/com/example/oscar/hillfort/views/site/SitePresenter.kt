package com.example.oscar.hillfort.views.site

import android.content.Intent
import com.example.oscar.hillfort.helpers.showImagePicker
import com.example.oscar.hillfort.main.MainApp
import com.example.oscar.hillfort.models.Location
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.location.LocationView
import org.jetbrains.anko.intentFor

class SitePresenter (val view: SiteView) {

    val IMAGE_0_REQUEST = 10
    val IMAGE_1_REQUEST = 11
    val IMAGE_2_REQUEST = 12
    val IMAGE_3_REQUEST = 13

    val LOCATION_REQUEST = 2

    var site = SiteModel()
    var location = Location(52.245696, -7.139102, 15f)
    var app: MainApp
    var edit = false;

    init {
        app = view.application as MainApp
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras.getParcelable<SiteModel>("site_edit")
            view.showSite(site)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String, date: String, isChecked: Boolean) {
        site.title = title
        site.description = description
        site.hasBeenVisited = isChecked
        site.notes = notes
        site.dateVisited = date
        if (edit) {
            app.sites.update(site)
        } else {
            app.sites.create(site)
        }
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        app.sites.delete(site)
        view.finish()
    }

    fun doSelectImage(imageNumber: Int) {
        showImagePicker(view, imageNumber)
    }

    fun doSetLocation() {
        if (site.zoom != 0f) {
            location.lat = site.lat
            location.lng = site.lng
            location.zoom = site.zoom
        }
        view.startActivityForResult(view.intentFor<LocationView>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_0_REQUEST -> {
                site.images[0] = data.data.toString(); view.updateImages(0, site.images[0])
            }
            IMAGE_1_REQUEST -> {
                site.images[1] = data.data.toString(); view.updateImages(1, site.images[1])
            }
            IMAGE_2_REQUEST -> {
                site.images[2] = data.data.toString(); view.updateImages(2, site.images[2])
            }
            IMAGE_3_REQUEST -> {
                site.images[3] = data.data.toString(); view.updateImages(3, site.images[3])
            }

            LOCATION_REQUEST -> {
                location = data.extras.getParcelable<Location>("location")
                site.lat = location.lat
                site.lng = location.lng
                site.zoom = location.zoom
            }
        }
    }
}
