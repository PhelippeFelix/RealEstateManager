package com.openclassrooms.realestatemanager.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.openclassrooms.realestatemanager.Database.Converters.Converters
import com.openclassrooms.realestatemanager.Database.Dao.EstateDao
import com.openclassrooms.realestatemanager.Database.Dao.ImageDao
import com.openclassrooms.realestatemanager.Database.Dao.LocationDao
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location


@Database(entities = [(Estate::class), (Image::class), (Location::class)],version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    abstract fun estateDao():EstateDao
    abstract fun imageDao():ImageDao
    abstract fun locationDao():LocationDao

    companion object {
        private var INSTANCE: RealEstateManagerDatabase? = null

        fun getInstance(context: Context):RealEstateManagerDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,RealEstateManagerDatabase::class.java,"RealEstateManager.db").build()
                }
            }
            return INSTANCE as RealEstateManagerDatabase
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}