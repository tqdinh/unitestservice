package com.example.myapplication

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import com.example.myapplication.Data.DataSource.ForegroundService.LocationTrackingService
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ServiceTest
{
    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var serviceIntent: Intent
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setup()
    {
        serviceIntent =Intent(ApplicationProvider.getApplicationContext(),LocationTrackingService::class.java)
        notificationManager = ApplicationProvider.getApplicationContext<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    @Test
    fun testForeGroundServiceStartStop()
    {
        val serviceIntent = Intent(ApplicationProvider.getApplicationContext(),LocationTrackingService::class.java)


        val binder = serviceRule.bindService(serviceIntent) as LocationTrackingService.TrackingBinder
        val service =binder.getService()

        assert(service!=null)


    }
}