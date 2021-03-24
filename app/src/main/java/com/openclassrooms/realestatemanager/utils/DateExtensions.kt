package com.openclassrooms.realestatemanager.utils

import java.text.SimpleDateFormat
import java.util.*


private val BASE_FORMAT = SimpleDateFormat("dd/MM/yyyy")

fun String.toFRDate() = BASE_FORMAT.parse(this)

fun Date.toFRString() = BASE_FORMAT.format(this)

fun Long.toFRDate() = Date(this)