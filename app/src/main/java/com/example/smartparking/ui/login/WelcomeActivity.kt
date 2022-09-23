package com.example.smartparking.ui.login

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_get_started.setOnClickListener {
            if(SmartParkingApplication.globalIsLoggedIn){
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}