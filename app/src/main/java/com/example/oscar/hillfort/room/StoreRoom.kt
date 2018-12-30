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

    override suspend fun findAll(): List<SiteModel> {
        val deferredSites = bg {
            dao.findAll()
        }
        return deferredSites.await()
    }

    override suspend fun findById(id: Long): SiteModel? {
        val deferredSite = bg {
            dao.findById(id)
        }
        val sites = deferredSite.await()
        return sites
    }

    override suspend fun create(site: SiteModel) {
        bg {
            dao.create(site)
        }
    }

    override suspend fun update(site: SiteModel) {
        bg {
            dao.update(site)
        }
    }

    override suspend fun delete(site: SiteModel) {
        bg {
            dao.deleteSite(site)
        }
    }

    override fun clear() {
    }
}