package com.example.smartparking.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsLoggedIn
import com.example.smartparking.ui.login.LoginActivity
import com.example.smartparking.ui.login.WelcomeActivity


class SettingsFragment :  PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null

        val button: Preference = findPreference("log_out_button")
        button.setOnPreferenceClickListener {
            globalIsLoggedIn = false
            val intent = Intent(context, WelcomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                context,
                "Logged out.",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }
}