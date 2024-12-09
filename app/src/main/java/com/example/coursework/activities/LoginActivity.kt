package com.example.coursework.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailText : EditText
    private lateinit var pwText : EditText

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intentInfo = intent.extras
        if (intentInfo != null && intentInfo.getBoolean("loggedOff")) {
            val view = findViewById<View>(R.id.loginView)
            displayMsg(view, getString(R.string.log_out_msg))
        }

        emailText = findViewById(R.id.loginEmailInput)
        pwText = findViewById(R.id.loginPassWordInput)

        val createAccountBtn = findViewById<Button>(R.id.loginCreateAccountBtn)
        createAccountBtn.setOnClickListener{_ -> createAccountActivityShow()}

        val signInBtn = findViewById<Button>(R.id.loginSignInBtn)
        signInBtn.setOnClickListener {v -> signIn(v)}
    }

    private fun createAccountActivityShow() {
        val accountCreationIntent = Intent(this, CreateAccountActivity::class.java)
        startActivity(accountCreationIntent)
    }

    private fun signIn(view: View) {
        if ((emailText.text.toString() != "") && (pwText.text.toString() != "")) {
            mAuth.signInWithEmailAndPassword(
                emailText.text.toString(),
                pwText.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    emailText.text.clear()
                    pwText.text.clear()
                    closeKeyBoard()
                    val homePageIntent = Intent(this, HomePageActivity::class.java)
                    startActivity(homePageIntent)
                }
            }.addOnFailureListener(this) { _ ->
                displayMsg(view, getString(R.string.login_error_msg))
            }
        } else {
            displayMsg(view, getString(R.string.login_error_msg))
        }
    }

    private fun displayMsg(view: View, msg: String) {
        val sb = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        val sbView = sb.view

        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        sbView.layoutParams = params

        sb.show()
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}