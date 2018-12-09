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

val JSON_FILE = "sites.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<SiteModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SiteJSONStore : SiteStore, AnkoLogger {

    val context: Context
    var sites = mutableListOf<SiteModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun delete(site: SiteModel) {
        sites.remove(site)
        serialize()
    }

    override fun findAll(): MutableList<SiteModel> {
        return sites
    }

    override fun findById(id: Long): SiteModel? {
        var foundsite:SiteModel? = sites.find{it.id == id}
        return foundsite
    }

    override fun create(site: SiteModel) {
        site.id = generateRandomId()
        sites.add(site)
        serialize()
    }

    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { it -> it.id == site.id }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.images = site.images
            foundSite.lat = site.lat
            foundSite.lng = site.lng
            foundSite.zoom = site.zoom
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
}