package com.example.oscar.hillfort.models

import android.content.Context
import com.example.oscar.hillfort.helpers.exists
import com.example.oscar.hillfort.helpers.read
import com.example.oscar.hillfort.helpers.write
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.util.*

const val JSON_FILE = "sites.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()!!
val listType = object : TypeToken<java.util.ArrayList<SiteModel>>() {}.type!!

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SiteJSONStore(val context: Context) : SiteStore, AnkoLogger {

    var sites = mutableListOf<SiteModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun delete(site: SiteModel) {
        sites.remove(site)
        serialize()
    }

    override suspend fun findAll(): MutableList<SiteModel> {
        return sites
    }

    override suspend fun findById(id: Long): SiteModel? {
        return sites.find { it.id == id }
    }

    override suspend fun create(site: SiteModel) {
        site.id = generateRandomId()
        sites.add(site)
        serialize()
    }

    override suspend fun update(site: SiteModel) {
        val foundSite: SiteModel? = sites.find { it -> it.id == site.id }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.images = site.images
            foundSite.dateVisited = site.dateVisited
            foundSite.lat = site.lat
            foundSite.lng = site.lng
            foundSite.zoom = site.zoom
            foundSite.rating = site.rating
            foundSite.favorite = site.favorite
            foundSite.hasBeenVisited = site.hasBeenVisited
            foundSite.notes = site.notes

            serialize()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sites, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        sites = Gson().fromJson(jsonString, listType)
    }

    override fun clear() {
        sites.clear()
    }
}