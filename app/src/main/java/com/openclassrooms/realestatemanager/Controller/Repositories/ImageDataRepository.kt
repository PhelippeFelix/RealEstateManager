package com.openclassrooms.realestatemanager.Controller.Repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Image
import io.reactivex.Observable


data class ImageDataRepository(private val database: RealEstateManagerDatabase) {

    // --- GET ---

    fun getImages(estateId:Long): LiveData<List<Image>> {
        return this.database.imageDao().getItems(estateId)
    }

    // --- CREATE ---

    fun createImage(image: Image) : Observable<Long> {
        return Observable.fromCallable{database.imageDao().insertItem(image)}
    }

    // --- UPDATE ---

    fun updateImage(image: Image) : Observable<Any>{
        return Observable.fromCallable{database.imageDao().updateItem(image)}
    }

    // --- DELETE ---

    fun deleteImage(image: Image) : Observable<Any>{
        return Observable.fromCallable{database.imageDao().deleteItem(image.id)}
    }
}