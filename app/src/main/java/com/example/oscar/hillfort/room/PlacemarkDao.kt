package org.wit.placemark.room

import androidx.room.*
import com.example.oscar.hillfort.models.SiteModel

@Dao
interface PlacemarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(placemark: SiteModel)

    @Query("SELECT * FROM SiteModel")
    fun findAll(): List<SiteModel>

    @Query("select * from SiteModel where id = :id")
    fun findById(id: Long): SiteModel

    @Update
    fun update(placemark: SiteModel)

    @Delete
    fun deletePlacemark(placemark: SiteModel)
}