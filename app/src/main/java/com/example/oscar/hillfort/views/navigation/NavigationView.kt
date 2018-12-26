package com.example.oscar.hillfort.views.navigation

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Toolbar
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationView() : BaseView(), GoogleMap.OnMarkerClickListener, Parcelable {

    private var toolbarNavigation: Toolbar? = null
    private var mapView: MapView? = null

    lateinit var presenter: NavigationPresenter

    constructor(parcel: Parcel) : this() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        init(toolbarNavigator, true)
        presenter = initPresenter(NavigationPresenter(this)) as NavigationPresenter

        mapView = findViewById<View>(R.id.mapView) as MapView

        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            presenter.doConfigureMap(it)
        }
    }

    // If Marker is clicked, this function is triggered
    override fun onMarkerClick(marker: Marker): Boolean {
        //presenter.doMarkerSelected(marker)
        return true
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
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavigationView> {
        override fun createFromParcel(parcel: Parcel): NavigationView {
            return NavigationView(parcel)
        }

        override fun newArray(size: Int): Array<NavigationView?> {
            return arrayOfNulls(size)
        }
    }
}