package com.example.oscar.hillfort.views

import android.content.Intent
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.Settings.SettingsView
import com.example.oscar.hillfort.views.location.LocationView
import com.example.oscar.hillfort.views.map.SiteMapsView
import com.example.oscar.hillfort.views.site.SiteView
import com.example.oscar.hillfort.views.siteList.SiteListView
import org.jetbrains.anko.AnkoLogger

enum class VIEW {
    LOCATION, SITE, MAPS, LIST, SETTINGS
}

abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, SiteListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, LocationView::class.java)
            VIEW.SITE -> intent = Intent(this, SiteView::class.java)
            VIEW.MAPS -> intent = Intent(this, SiteMapsView::class.java)
            VIEW.LIST -> intent = Intent(this, SiteListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun updateImage(code: Int, imagePath: String) {}
    open fun showSite(site: SiteModel) {}
    open fun showSites(sites: List<SiteModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}