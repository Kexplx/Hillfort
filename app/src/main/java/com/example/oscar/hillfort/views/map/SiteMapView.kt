package com.example.oscar.hillfort.views.map

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_site_maps.*
import kotlinx.android.synthetic.main.content_site_maps.*

class SiteMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: SiteMapPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_maps)
        super.init(toolbarMaps, true)

        presenter = initPresenter(SiteMapPresenter(this)) as SiteMapPresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadSites()
        }
    }

    override fun showSite(site: SiteModel) {
        currentTitle.text = site.title
        currentDescripton.text = site.description
        Glide.with(this).load(site.images[0]).into(imageView)
    }

    override fun showSites(sites: List<SiteModel>) {
        presenter.doPopulateMap(map, sites)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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