package com.example.oscar.hillfort.room

import android.content.Context
import androidx.room.Room
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.models.SiteStore
import org.jetbrains.anko.coroutines.experimental.bg

class StoreRoom(val context: Context) : SiteStore {

    var dao: SiteDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.siteDao()
    }

    suspend override fun findAll(): List<SiteModel> {
        val deferredSites = bg {
            dao.findAll()
        }
        val placemarks = deferredSites.await()
        return placemarks
    }

    suspend override fun findById(id: Long): SiteModel? {
        val deferredSite = bg {
            dao.findById(id)
        }
        val placemark = deferredSite.await()
        return placemark
    }

    suspend override fun create(site: SiteModel) {
        bg {
            dao.create(site)
        }
    }

    suspend override fun update(site: SiteModel) {
        bg {
            dao.update(site)
        }
    }

    suspend override fun delete(site: SiteModel) {
        bg {
            dao.deleteSite(site)
        }
    }

    fun clear() {
    }
}