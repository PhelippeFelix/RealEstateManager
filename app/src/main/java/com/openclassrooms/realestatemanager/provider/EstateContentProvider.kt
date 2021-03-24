package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Estate


class EstateContentProvider : ContentProvider() {

    // FOR DATA
    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Estate::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun insert(uri: Uri, p1: ContentValues?): Uri {
       if (context != null && p1 != null){
           Log.e("EstateContentProvider","ContentValues : $p1")
           val index = RealEstateManagerDatabase.getInstance(context!!).estateDao().insertItem(Estate().fromContentValues(p1))
           if (index != 0L){
               context!!.contentResolver.notifyChange(uri,null)
               return ContentUris.withAppendedId(uri,index)
           }
       }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun query(uri: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        if (context != null){
            val index:Long = ContentUris.parseId(uri)
            val cursor = RealEstateManagerDatabase.getInstance(context!!).estateDao().getItemsWithCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun onCreate(): Boolean {
      return true
    }

    override fun update(uri: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
       if (context != null && p1 != null){
           val count:Int = RealEstateManagerDatabase.getInstance(context!!).estateDao().updateItem(Estate().fromContentValues(p1))
           context!!.contentResolver.notifyChange(uri,null)
           return count
       }

        throw IllegalArgumentException("Failed to update row into $uri")
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
       throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(uri: Uri): String {
       return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }
}