package com.example.oscar.hillfort.models

interface SiteStore {
    fun create(site: SiteModel)
    fun findAll(): List<SiteModel>
    fun delete(site: SiteModel)
    fun findById(id:Long) : SiteModel?
    fun update(site: SiteModel)
}