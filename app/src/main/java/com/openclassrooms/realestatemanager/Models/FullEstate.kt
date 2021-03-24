package com.openclassrooms.realestatemanager.Models

import androidx.room.Embedded
import androidx.room.Relation


class FullEstate {

    @Embedded
    lateinit var estate:Estate

    @Relation(parentColumn = "id",
            entityColumn = "estateId")
    var images:List<Image> = arrayListOf()

    @Embedded
    lateinit var location:Location
}