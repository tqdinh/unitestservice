package com.example.myapplication.Data.DataSource.ForegroundService

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationTrackingService : Service() {

    companion object{
        private const val CHANNEL_ID = "location_tracking_channel"
        private const val NOTIFICATION_ID = 1
        private const val ACTION_STOP_SERVICE = "STOP_SERVICE"
    }

    val serviceJob =Job()
    val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    val binder = TrackingBinder()

    @SuppressLint("NewApi")
    @Override
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(CHANNEL_ID,"Location tracking", NotificationManager.IMPORTANCE_DEFAULT)
        getSystemService(NotificationManager::class.java)?.let {
            it.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        if(ACTION_STOP_SERVICE == intent?.action)
        {
            stopForeground()
            return START_NOT_STICKY
        }
        else{
            startForeground(NOTIFICATION_ID,createNotification())
            startPeriod()
        }
        return START_STICKY
    }


    @SuppressLint("NewApi")
    fun createNotification():Notification
    {

        val stopIntent = Intent(this@LocationTrackingService ,LocationTrackingService::class.java).apply {
            action= ACTION_STOP_SERVICE
        }
        val stopPending =PendingIntent.getService(this@LocationTrackingService,0,stopIntent,PendingIntent.FLAG_MUTABLE)

        return NotificationCompat.Builder(this@LocationTrackingService, CHANNEL_ID)
            .setContentTitle("Running")
            .setSmallIcon(R.drawable.app_icon)
            .addAction(R.drawable.ic_launcher_background,"Stop",stopPending)
            .build()

    }

    fun startPeriod()
    {
        serviceScope.launch {
            var i=0
            while (true)
            {
                i+=1
                delay(1000)
                Log.d("BGTHREAD","${i}")
            }
        }

    }

    fun stopForeground()
    {
        stopForeground(true)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

    }


    inner class TrackingBinder : Binder() {
        fun getService () = this@LocationTrackingService
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}