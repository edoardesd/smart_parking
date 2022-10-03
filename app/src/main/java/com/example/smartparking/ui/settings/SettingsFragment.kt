package com.example.smartparking.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsLoggedIn


class SettingsFragment :  PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null

        val buttonWelcome: Preference = findPreference("log_out_button")
        buttonWelcome.setOnPreferenceClickListener {
            globalIsLoggedIn = false
//            arguments?.clear()
            val actionDetail = SettingsFragmentDirections.actionSettingsFragmentToWelcomeActivity()
            view?.let { it1 -> Navigation.findNavController(it1).navigate(actionDetail) }

            Toast.makeText(
                context,
                "Logged out.",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        val buttonSwitch: Preference = findPreference("switch_user_button")
        buttonSwitch.setOnPreferenceClickListener {
            globalIsLoggedIn = false
//            arguments?.clear()
            val actionDetail = SettingsFragmentDirections.actionSettingsFragmentToChangeUserActivity()
            view?.let { it1 -> Navigation.findNavController(it1).navigate(actionDetail) }

            Toast.makeText(
                context,
                "Change user.",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    override fun onDestroyView() {
        arguments?.clear()
        super.onDestroyView()
    }
}