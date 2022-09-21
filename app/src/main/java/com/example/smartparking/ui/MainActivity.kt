package com.example.smartparking.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
//import br.com.mauker.materialsearchview.MaterialSearchView
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.ui.parking.control.ControlFragment
import com.example.smartparking.ui.parking.navigation.choice.NavigationChoiceFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import com.example.smartparking.ui.settings.SettingsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*

private const val MY_PERMISSION_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
//    private lateinit var searchView: MaterialSearchView


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


//
//        var datetime = Date(2022, 8, 22)
//        datetime.hours = 10
//        datetime.minutes = 15
//        var displayLesson = ArrayList<LessonListModel>()
//
//        displayLesson.add(LessonListModel("Lesson blbla", "room 2", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 2", "room 1", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 3", "room 4", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 4", "room 5", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 5", "room 21", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 6", "room 23", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//        displayLesson.add(LessonListModel("Lesson blbla 7", "room 45", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
//
////
//        searchView = findViewById(R.id.search_view)
//
//        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(applicationContext, "asd", Toast.LENGTH_LONG).show()
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//        })
//
//        searchView.setSearchViewListener(object : MaterialSearchView.SearchViewListener {
//            override fun onSearchViewOpened() {
//                Toast.makeText(applicationContext, "open", Toast.LENGTH_LONG).show()
//
//                // Do something once the view is open.
//            }
//
//            override fun onSearchViewClosed() {
//                // Do something once the view is closed.
//            }
//        })

//        searchView.setOnItemClickListener { _, _, position, _ -> // Do something when the suggestion list is clicked.
//            val suggestion = searchView.getSuggestionAtPosition(position)
//            searchView.setQuery(suggestion, false)
//        }
//        searchView.setOnClearClickListener {
//            Toast.makeText(this, "Clear clicked!", Toast.LENGTH_LONG).show()
//        }
//
//        searchView.adjustTintAlpha(0.4f)
//        val context: Context = this
//        searchView.setOnItemLongClickListener { _, _, i, _ ->
//            Toast.makeText(context, "Long clicked position: $i", Toast.LENGTH_SHORT).show()
//            true
//        }
//        // This will override the default audio action.
//        searchView.setOnVoiceClickedListener { Toast.makeText(context, "Voice clicked!", Toast.LENGTH_SHORT).show() }
//

        requestLocationPermission()
        if (hasLocationPermission()) {
            bindLocationManager()
        }
        else
            requestLocationPermission()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_item, menu)
//        val menuItem = menu!!.findItem(R.id.action_search)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle toolbar item clicks here. It'll
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_search -> {
//                // Open the search view on the menu item click.
//                searchView.openSearch()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback)
    }




//    override fun onBackPressed() {
//        if (searchView.isOpen) {
//            // Close the search on the back button press.
//            searchView.closeSearch()
//        } else {
//            super.onBackPressed()
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
//            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            if (matches != null && matches.size > 0) {
//                val searchWrd = matches[0]
//                if (!TextUtils.isEmpty(searchWrd)) {
//                    searchView.setQuery(searchWrd, false)
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        searchView.clearSuggestions()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        searchView.activityResumed()
//        val arr = resources.getStringArray(R.array.unitSystemEntries)
//        searchView.addSuggestions(arr)
//    }

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