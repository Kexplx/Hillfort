package com.example.oscar.hillfort.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.oscar.hillfort.models.SiteModel

@Database(entities = arrayOf(SiteModel::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun siteDao(): SiteDao
}