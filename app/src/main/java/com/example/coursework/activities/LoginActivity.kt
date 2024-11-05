package com.example.coursework.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intentInfo = intent.extras
        if (intentInfo != null && intentInfo.getBoolean("loggedOff")) {
            showLoggedOffSnackBar()
        }

        val createAccountBtn = findViewById<Button>(R.id.loginCreateAccountBtn)
        createAccountBtn.setOnClickListener{_ -> createAccountActivityShow()}

        val signInBtn = findViewById<Button>(R.id.loginSignInBtn)
        signInBtn.setOnClickListener {_ -> homePageActivityShow()}
    }

    private fun createAccountActivityShow() {
        val accountCreationIntent = Intent(this, CreateAccountActivity::class.java)
        startActivity(accountCreationIntent)
    }

    private fun homePageActivityShow() {
        val homePageIntent = Intent(this, HomePageActivity::class.java)
        startActivity(homePageIntent)
        Log.d("MainActivity", "onCreate: Started SecondActivity");

    }

    private fun showLoggedOffSnackBar() {
        val view = findViewById<View>(R.id.main)
        val snackBar =
            Snackbar.make(view, "You Have Logged Off", Snackbar.LENGTH_SHORT)
        snackBar.show()
    }
}