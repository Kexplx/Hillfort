package com.example.oscar.hillfort.views.site

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BaseView
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_site.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class SiteView : BaseView(), AnkoLogger {

    lateinit var presenter: SitePresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)
        init(toolbarSite, true)

        presenter = initPresenter(SitePresenter(this)) as SitePresenter

        chkVisited.setOnClickListener {
            if (chkVisited.isChecked) {
                txtDateVisited.isEnabled = true
            } else {
                txtDateVisited.text = null
                txtDateVisited.isEnabled = false
            }
        }

        imgBtn0.setOnClickListener { presenter.doSelectImage(presenter.IMAGE_0_REQUEST) }
        imgBtn1.setOnClickListener { presenter.doSelectImage(presenter.IMAGE_1_REQUEST) }
        imgBtn2.setOnClickListener { presenter.doSelectImage(presenter.IMAGE_2_REQUEST) }
        imgBtn3.setOnClickListener { presenter.doSelectImage(presenter.IMAGE_3_REQUEST) }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }
    }

    override fun showSite(site: SiteModel) {
        txtSiteName.setText(site.title)
        txtSiteDescription.setText(site.description)
        txtAdditionNotes.setText(site.notes)
        txtDateVisited.setText(site.dateVisited)
        chkVisited.isChecked = site.hasBeenVisited
        favorite.isChecked = site.favorite
        ratingBar.rating = site.rating

        if (site.images[0] != "") {
            Glide.with(this).load(site.images[0]).into(imgBtn0);
        }
        if (site.images[1] != "") {
            Glide.with(this).load(site.images[1]).into(imgBtn1);
        }
        if (site.images[2] != "") {
            Glide.with(this).load(site.images[2]).into(imgBtn2);
        }
        if (site.images[3] != "") {
            Glide.with(this).load(site.images[3]).into(imgBtn3);
        }
    }

    override fun updateImage(code: Int, imagePath: String) {
        when (code) {
            0 -> Glide.with(this).load(imagePath).into(imgBtn0);
            1 -> Glide.with(this).load(imagePath).into(imgBtn1);
            2 -> Glide.with(this).load(imagePath).into(imgBtn2);
            3 -> Glide.with(this).load(imagePath).into(imgBtn3);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        if (presenter.edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_save -> {
                if (txtSiteName.text.toString().isEmpty()) {
                    toast(getString(R.string.toast_TitelEinfügen))
                } else {
                    presenter.doAddOrSave(
                        txtSiteName.text.toString(), txtSiteDescription.text.toString(),
                        txtAdditionNotes.text.toString(), txtDateVisited.text.toString(),
                        chkVisited.isChecked, favorite.isChecked, ratingBar.rating
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}