package com.openclassrooms.realestatemanager.Database.Converters

import androidx.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

    @TypeConverter fun dateToTimestamp(date: Date?): Long? = date?.time
}