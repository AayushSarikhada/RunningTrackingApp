package com.aayush.runningappaayush.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aayush.runningappaayush.R
import com.aayush.runningappaayush.other.Constants.ACTION_PAUSE_SERVICE
import com.aayush.runningappaayush.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.aayush.runningappaayush.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.aayush.runningappaayush.other.Constants.ACTION_STOP_SERVICE
import com.aayush.runningappaayush.other.Constants.FASTEST_LOCATION_INTERVAL
import com.aayush.runningappaayush.other.Constants.LOCATION_UPDATE_INTERVAL
import com.aayush.runningappaayush.other.Constants.NOTIFICATION_CHANNEL_ID
import com.aayush.runningappaayush.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.aayush.runningappaayush.other.Constants.NOTIFICATION_ID
import com.aayush.runningappaayush.other.Utils
import com.aayush.runningappaayush.ui.MainActivity
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber


typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService:LifecycleService() {

    var isFirstRun = true
    lateinit var fusedLocationProvideClient: FusedLocationProviderClient
    companion object{
        val isTracking = MutableLiveData<Boolean>()

        val pathPoints = MutableLiveData<PolyLines>()

    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProvideClient =
            LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){

                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        Timber.d("Running Service...")
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        }else{
            FLAG_UPDATE_CURRENT
        }
    )

    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    val locationCallback = object :LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let{locations->
                    for(location in locations){
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude},${location.longitude}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking:Boolean){
        if(isTracking){
            if(Utils.hasLocationPermissions(this) && Utils.hasBackgroundPermission(this)){
                val request = com.google.android.gms.location.LocationRequest.Builder(
                    LOCATION_UPDATE_INTERVAL).apply {
                    setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL)
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)

                }.build()

                fusedLocationProvideClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )

            }
        }
        else{
            fusedLocationProvideClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun addPathPoint(location:Location?){
        location?.let{
            val pos = LatLng(location.latitude,location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }

        }
    }

    private fun startForegroundService(){
        addEmptyPolyLine()
        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")         //initial time
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }

}