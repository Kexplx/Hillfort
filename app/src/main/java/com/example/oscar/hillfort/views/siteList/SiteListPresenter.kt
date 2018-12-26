package com.example.oscar.hillfort.views.siteList

import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SiteListPresenter(view: SiteListView) : BasePresenter(view) {

    fun loadSites() {
        async(UI) {
            view?.showSites(app.sites.findAll())
        }
    }

    fun loadSitesWithFilter(filter: String) {
        async(UI) {
            view?.showSites(app.sites.findAll().filter { x -> x.title.startsWith(filter) })
        }
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

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowNavigator() {
        view?.navigateTo(VIEW.NAVIGATION)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.sites.clear()
        view?.navigateTo(VIEW.LOGIN)
    }
}