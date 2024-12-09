package com.example.coursework.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var nameText : EditText
    private lateinit var emailText : EditText
    private lateinit var pwText : EditText
    private lateinit var reEnterPwText : EditText
    private lateinit var regBtn : Button

    private var mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        nameText = findViewById(R.id.createAccountNameInput)
        emailText = findViewById(R.id.createAccountEmailInput)
        pwText = findViewById(R.id.createAccountPassWordInput)
        reEnterPwText = findViewById(R.id.reEnterPassWordInput)
        regBtn = findViewById(R.id.createAccountBtn)

        regBtn.setOnClickListener{v -> createAccount(v)}
    }

    private fun createAccount(view: View) {
        if(mAuth.currentUser != null) {
            displayMsg(view, getString(R.string.user_already_logged_in))
        } else if ((pwText.text.toString() == reEnterPwText.text.toString()) && fieldsNotEmpty()) {
            mAuth.createUserWithEmailAndPassword(
                emailText.text.toString(),
                pwText.text.toString()
            ).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    closeKeyBoard()
                    nameText.text.clear()
                    emailText.text.clear()
                    pwText.text.clear()
                    reEnterPwText.text.clear()
                    val homePageIntent = Intent(this, HomePageActivity::class.java)
                    startActivity(homePageIntent)
                }
            }.addOnFailureListener(this) { _ ->
                displayMsg(view, getString(R.string.account_creation_error_msg))
            }
        } else {
            displayMsg(view, getString(R.string.empty_fields_msg))
        }
    }

    private fun fieldsNotEmpty(): Boolean {
        var notEmpty = false

        if ((nameText.text.toString() != "")
            && (emailText.text.toString() != "")
            && (pwText.text.toString() != "")
            && (reEnterPwText.text.toString() != "")) {
            notEmpty = true
        }

        return notEmpty
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