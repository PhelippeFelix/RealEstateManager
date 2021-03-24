package com.openclassrooms.realestatemanager.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import android.util.Log
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.R


class Notifications {

    private val notificationID = 105

    private lateinit var mContext:Context
    private lateinit var mNotificationManager:NotificationManager
    private lateinit var mBuilder:NotificationCompat.Builder

    private val channelID = "com.openclassrooms.realestatemanager"
    private val channelName = "RealEstateManager"

    /**
     * Create and push the notification
     */
    fun sendNotification(context: Context,action:String) {
        this.mContext = context
        Log.e("TAG", "sendNotification: ")
        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationMessage:String = if (action == Constants.VIEW_MODEL_ACTION_CREATE){
            context.resources.getString(R.string.notification_message_create)
        }else{
            context.resources.getString(R.string.notification_message_update)
        }

        //Build notification
        val notification = buildLocalNotification(mContext, pendingIntent,notificationMessage).build()

        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelID, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            mBuilder.setChannelId(channelID)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        mNotificationManager.notify(notificationID, notification)
    }

    private fun buildLocalNotification(mContext: Context, pendingIntent: PendingIntent, message:String): NotificationCompat.Builder {
        Log.e("TAG", "buildLocalNotification: ")
        mBuilder = NotificationCompat.Builder(mContext, channelID)
        mBuilder.setSmallIcon(R.drawable.baseline_location_city_black_24)
        mBuilder.setContentTitle(mContext.resources.getString(R.string.notification_title))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        return mBuilder
    }
}