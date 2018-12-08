package com.example.oscar.hillfort.main

import android.app.Application
import com.example.oscar.hillfort.models.SiteJSONStore
import com.example.oscar.hillfort.models.SiteStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    lateinit var sites: SiteStore

    override fun onCreate() {
        super.onCreate()
        sites = SiteJSONStore(applicationContext)
        info("Hillfort started")
    }
}