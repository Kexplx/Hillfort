package com.example.oscar.hillfort.main

import android.app.Application
import com.example.oscar.hillfort.models.SiteFireStore
import com.example.oscar.hillfort.models.SiteStore
import org.jetbrains.anko.AnkoLogger

class MainApp : Application(), AnkoLogger {
    lateinit var sites: SiteStore

    override fun onCreate() {
        super.onCreate()
        sites = SiteFireStore(applicationContext)
    }
}