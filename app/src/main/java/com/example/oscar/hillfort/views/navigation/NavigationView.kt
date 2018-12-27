package com.example.oscar.hillfort.views.navigation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationView : BaseView(), GoogleMap.OnMarkerClickListener {

    private var mapView: MapView? = null

    lateinit var presenter: NavigationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        init(toolbarNavigator, true)

        mapView = findViewById<View>(R.id.mapView) as MapView

        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMarkerClickListener(this)
        }

        presenter = initPresenter(NavigationPresenter(this)) as NavigationPresenter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                presenter.stopLocationUpdates(); presenter.firstZoomSet = false
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.setNewDestination(marker)
        return true
    }

    override fun updateCardWithDestination(site: SiteModel, distanceInMeters: Double) {
        txtHint.visibility = View.GONE
        currentTitle.text = site.title
        currentDescription.text = site.description
        txtCurrrentDistance.text = distanceInMeters.toInt().toString()
        currentDistance.text = getString(R.string.distance_in_meters)
        Glide.with(this).load(site.images[0]).into(imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        presenter.stopLocationUpdates()
        super.onBackPressed()
    }
}