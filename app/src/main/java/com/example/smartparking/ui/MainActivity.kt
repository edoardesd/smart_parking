package com.example.smartparking.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.smartparking.R
import com.example.smartparking.ui.parking.control.disabled.ControlDisabledFragment
import com.example.smartparking.ui.parking.navigation.choice.NavigationChoiceFragment
import com.example.smartparking.ui.settings.SettingsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.jar.Manifest

private const val MY_PERMISSION_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    val navigateFragment = NavigationChoiceFragment()
    val controlFragment = ControlDisabledFragment()
    val settingFragment = SettingsFragment()

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.navigationChoiceFragment -> {Log.d(TAG, "first")
                    }

                    R.id.controlDisabledFragment -> Log.d(TAG, "second")

                    R.id.settingsFragment -> Log.d(TAG, "third")
                }
            }

//        bottom_nav.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navigationChoiceFragment -> setCurrentFragment(navigateFragment)
//                R.id.controlDisabledFragment -> setCurrentFragment(controlFragment)
//                R.id.settingsFragment -> setCurrentFragment(settingFragment)
//            }
//            true
//        }


        requestLocationPermission()
        if (hasLocationPermission()) {
            bindLocationManager()
        }
        else
            requestLocationPermission()

    }

//    private fun setCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.nav_host_fragment, fragment)
//            commit()
//        }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), null)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSION_ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permission: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else
                Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG)
                    .show()
//        super.onRequestPermissionsResult(requestCode, permission, grantResults)
        }
    }

}