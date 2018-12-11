package com.example.oscar.hillfort.views.map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.helpers.readImageFromPath
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_site_maps.*
import kotlinx.android.synthetic.main.content_site_maps.*

class SiteMapsView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {
    lateinit var presenter: SiteMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_maps)
        setSupportActionBar(toolbarMaps)
        mapView.onCreate(savedInstanceState);

        presenter = SiteMapPresenter(this)

        mapView.getMapAsync {
            presenter.map = it
            presenter.configureMap()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val site = presenter.app.sites.findById(tag)
        currentTitle.text = site!!.title
        currentDescripton.text = site.description
        imageView.setImageBitmap(readImageFromPath(this@SiteMapsView, site.images[0]))
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}