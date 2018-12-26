package com.example.oscar.hillfort.views.Settings

import com.example.oscar.hillfort.views.BasePresenter
import com.google.firebase.auth.FirebaseAuth

class SettingsPresenter(view: SettingsView) : BasePresenter(view) {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun doGetTotalNumberOfSites(): Int {
        return app.sites.findAll().count()
    }

    suspend fun doGetTotalNumberOfVisitedSites(): Int {
        return app.sites.findAll().count { x -> x.hasBeenVisited }
    }

    fun doGetUsername(): String {
        return FirebaseAuth.getInstance().currentUser!!.email!!
    }

    fun doChangePassword(password: String) {


        val currentUser = auth.currentUser
        currentUser?.updatePassword(password)
    }
}