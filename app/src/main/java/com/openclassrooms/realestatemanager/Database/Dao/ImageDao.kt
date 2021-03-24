package com.openclassrooms.realestatemanager.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.Models.Image


@Dao
interface ImageDao {

    @Query("SELECT * FROM Image WHERE estateId = :estateId")
    fun getItems(estateId:Long): LiveData<List<Image>>

    @Insert
    fun insertItem(image: Image) : Long

    @Update
    fun updateItem(image: Image)

    @Query("DELETE FROM Image WHERE id = :index")
    fun deleteItem(index: Long)
}