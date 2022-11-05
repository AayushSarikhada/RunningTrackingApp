package com.aayush.runningappaayush.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aayush.runningappaayush.R
import com.aayush.runningappaayush.other.Constants
import com.aayush.runningappaayush.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    //scoped to our Tracking service so it will only live unitil our TRacking service does

    @ServiceScoped //only one instance for lifetime of service
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ) = LocationServices.getFusedLocationProviderClient(app)


    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app:Context
    ) =  PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        },
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("TRACKINGSERVICE","here1")
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        }else{
            Log.d("TRACKINGSERVICE","here2")
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    )

    @ServiceScoped
    @Provides
    fun baseNotificationBuilder(
        @ApplicationContext app:Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
        .setContentTitle("Running App")
        .setContentText("00:00:00")         //initial time
        .setContentIntent(pendingIntent)

}