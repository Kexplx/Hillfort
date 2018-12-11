package com.example.oscar.hillfort.views.siteList

import com.example.oscar.hillfort.main.MainApp
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.map.SiteMapsView
import com.example.oscar.hillfort.views.site.SiteView
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class SiteListPresenter(val view: SiteListView) {

    var app: MainApp = view.applicationContext as MainApp

    fun getSites() = app.sites.findAll()

    fun doAddSite() {
        view.startActivityForResult<SiteView>(0)
    }

    fun doEditSite(site: SiteModel) {
        view.startActivityForResult(view.intentFor<SiteView>().putExtra("site_edit", site), 0)
    }

    fun doShowSitesMap() {
        view.startActivity<SiteMapsView>()
    }
}