package com.openclassrooms.realestatemanager.Controller.Repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Location
import io.reactivex.Observable


class LocationDataRepository(private val database: RealEstateManagerDatabase) {

    // --- GET ---

    fun getLocation(estateId:Long): LiveData<List<Location>> {
        return this.database.locationDao().getItems(estateId)
    }

    fun getSectorList() : Observable<List<String>>{
        return Observable.fromCallable{database.locationDao().getSectorList()}
    }

    fun getLocationId(estateId:Long): Observable<Long> {
        return Observable.fromCallable{database.locationDao().getItemId(estateId)}
    }

    // --- CREATE ---

    fun createLocation(location: Location) : Observable<Long> {
        return Observable.fromCallable{database.locationDao().insertItem(location)}
    }

    // --- UPDATE ---

    fun updateLocation(location: Location): Observable<Any>{
        return Observable.fromCallable{database.locationDao().updateItem(location)}
    }
}