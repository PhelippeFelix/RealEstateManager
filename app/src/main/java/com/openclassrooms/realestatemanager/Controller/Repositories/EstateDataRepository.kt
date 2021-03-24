package com.openclassrooms.realestatemanager.Controller.Repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import io.reactivex.Observable


class EstateDataRepository(private val database: RealEstateManagerDatabase) {

    // --- GET ---

    fun getEstates(): LiveData<List<FullEstate>> {
        return this.database.estateDao().getItems()
    }

    fun gesEstatesBySearch(query:SimpleSQLiteQuery) : LiveData<List<FullEstate>>{
        return this.database.estateDao().getItemsBySearch(query)
    }

    fun gesEstateByID(estateID:Long) : LiveData<FullEstate>{
        return this.database.estateDao().getItemsByID(estateID)
    }

    fun nuke(){
        this.database.estateDao().nukeTable()
    }

    // --- CREATE ---

    fun createEstate(estate: Estate) : Observable<Long> {
       return Observable.fromCallable{database.estateDao().insertItem(estate)}
    }

    // --- UPDATE ---
    fun updateEstate(estate: Estate) : Observable<Any> {
      return Observable.fromCallable{database.estateDao().updateItem(estate)}
    }

}