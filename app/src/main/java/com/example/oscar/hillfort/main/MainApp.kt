package com.example.oscar.hillfort.main

import android.app.Application
import com.example.oscar.hillfort.models.SiteStore
import com.example.oscar.hillfort.room.StoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    lateinit var sites: SiteStore

    override fun onCreate() {
        super.onCreate()
        sites = StoreRoom(applicationContext)
        info("Hillfort started")
    }
}