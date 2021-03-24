package com.openclassrooms.realestatemanager.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "locationId") var id:Long,
                    var address:String?,
                    var additionalAddress:String?,
                    var sector:String?,
                    var city:String?,
                    var zipCode:String?,
                    var country:String?,
                    var estateId:Long)