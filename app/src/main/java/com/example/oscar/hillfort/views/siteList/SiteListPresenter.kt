package com.example.oscar.hillfort.views.siteList

import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.VIEW

class SiteListPresenter(view: SiteListView) : BasePresenter(view) {

    fun showSites() {
        view?.showSites(app.sites.findAll())
    }

    fun doAddSite() {
        view?.navigateTo(VIEW.SITE)
    }

    fun doEditSite(site: SiteModel) {
        view?.navigateTo(VIEW.SITE, 0, "site_edit", site)
    }

    fun doShowSitesMap() {
        view?.navigateTo(VIEW.MAPS)
    }
}