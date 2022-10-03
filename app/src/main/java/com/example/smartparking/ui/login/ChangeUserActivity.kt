package com.example.smartparking.ui.login

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.preference.Preference
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsLoggedIn
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalUserType
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.settings.SettingsFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_user.*

class ChangeUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user)

        initButtons()
    }

    private fun initButtons() {
        btn_student_architecture.setOnClickListener {
            globalUserType = UserType.STUDENT_ARCHITECTURE
            setIntent()
            makeToast()
        }

        btn_student_engineering.setOnClickListener {
            globalUserType = UserType.STUDENT_ENGINEERING
            setIntent()
            makeToast()
        }

        btn_prof_architecture.setOnClickListener {
            globalUserType = UserType.PROF_ARCHITECTURE
            setIntent()
            makeToast()
        }

        btn_prof_engineering.setOnClickListener {
            globalUserType = UserType.PROF_ENGINEERING
            setIntent()
            makeToast()
        }

        btn_guest.setOnClickListener {
            globalUserType = UserType.GUEST
            setIntent()
            makeToast()
        }
    }

    private fun makeToast() {
        Toast.makeText(
            this@ChangeUserActivity,
            "Change user.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setIntent() {
        globalIsLoggedIn = true
        val intent = Intent(this@ChangeUserActivity, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}