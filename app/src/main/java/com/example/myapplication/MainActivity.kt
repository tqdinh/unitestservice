package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.myapplication.Data.DataSource.ForegroundService.LocationTrackingService
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.POST_NOTIFICATIONS
    )


    private var locationTrackingService: LocationTrackingService? = null

    var isBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationTrackingService.TrackingBinder
            locationTrackingService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )

            Intent(this, LocationTrackingService::class.java).also { intent ->
                this@MainActivity.startForegroundService(intent)
            }

        requestPermissions()
    }


    private val cameraPemission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Read external storage permission denied!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val locationPermisionRequests =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            if (permissionMap.all { it.value })
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT)
                    .show()
            else
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT)
                    .show()

        }


    private fun requestPermissions() {

        val notGranted = locationPermissions.filterNot { permission ->
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            val showRationable =
                notGranted.any { permiss -> shouldShowRequestPermissionRationale(permiss) }

            if (showRationable) {
                AlertDialog.Builder(this)
                    .setTitle("Permissions")
                    .setMessage("Permissions is needed in order to show images and videos")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        Toast.makeText(
                            this,
                            "Read media storage permission denied!",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { _, _ ->
                        locationPermisionRequests.launch(notGranted.toTypedArray())
                    }
                    .show()
            } else {
                locationPermisionRequests.launch(notGranted.toTypedArray())
            }

        }


    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}