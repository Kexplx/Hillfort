package com.example.oscar.hillfort.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.oscar.hillfort.helpers.GithubTypeConverters
import com.example.oscar.hillfort.models.SiteModel

@Database(entities = arrayOf(SiteModel::class), version = 1)
@TypeConverters(GithubTypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun siteDao(): SiteDao
}