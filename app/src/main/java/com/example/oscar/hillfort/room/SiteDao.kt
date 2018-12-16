package com.example.oscar.hillfort.room

import androidx.room.*
import com.example.oscar.hillfort.models.SiteModel

@Dao
interface SiteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(site: SiteModel)

    @Query("SELECT * FROM SiteModel")
    fun findAll(): List<SiteModel>

    @Query("select * from SiteModel where id = :id")
    fun findById(id: Long): SiteModel

    @Update
    fun update(site: SiteModel)

    @Delete
    fun deleteSite(site: SiteModel)
}