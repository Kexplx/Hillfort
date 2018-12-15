package com.example.oscar.hillfort.views.site

import android.content.Intent
import com.example.oscar.hillfort.helpers.showImagePicker
import com.example.oscar.hillfort.models.Location
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.VIEW

class SitePresenter(view: SiteView) : BasePresenter(view) {

    val IMAGE_0_REQUEST = 10
    val IMAGE_1_REQUEST = 11
    val IMAGE_2_REQUEST = 12
    val IMAGE_3_REQUEST = 13

    val LOCATION_REQUEST = 2

    var site = SiteModel()
    var location = Location(52.245696, -7.139102, 15f)
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras.getParcelable("site_edit")
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

    fun doSetLocation() {
        if (edit == false) {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
        } else {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(site.lat, site.lng, site.zoom))
        }
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
                location = data.extras.getParcelable("location")
                site.lat = location.lat
                site.lng = location.lng
                site.zoom = location.zoom
            }
        }
    }
}
