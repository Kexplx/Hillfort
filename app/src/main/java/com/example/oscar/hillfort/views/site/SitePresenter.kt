package com.example.oscar.hillfort.views.site

import android.content.Intent
import com.example.oscar.hillfort.helpers.showImagePicker
import com.example.oscar.hillfort.main.MainApp
import com.example.oscar.hillfort.models.Location
import com.example.oscar.hillfort.models.SiteModel
import org.jetbrains.anko.intentFor

class SitePresenter (val view: SiteView) {

    val IMAGE_REQUEST = 1
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
            view.showPlacemark(site)
        }
    }

    fun doAddOrSave(title: String, description: String) {
        site.title = title
        site.description = description
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

    fun doSelectImage() {
        showImagePicker(view, IMAGE_REQUEST)
    }

    fun doSetLocation() {
        if (site.zoom != 0f) {
            location.lat = site.lat
            location.lng = site.lng
            location.zoom = site.zoom
        }
//        view.startActivityForResult(view.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                when {
                    site.images[0] == "" -> site.images[0]=  data.data.toString()
                    site.images[1] == "" -> site.images[1] = data.data.toString()
                    site.images[2] == "" -> site.images[2] = data.data.toString()
                    site.images[3] == "" -> site.images[2] = data.data.toString()
                    else -> site.images[0] = data.data.toString()
                }
                view.showPlacemark(site)
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