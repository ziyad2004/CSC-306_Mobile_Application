package com.example.coursework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import org.json.JSONArray

class HomePageActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        Log.e("MainActivity", "Error starting SecondActivity")

        val mToolBar = findViewById<Toolbar>(R.id.homePageAppBar)
        setSupportActionBar(mToolBar)

        supportActionBar?.title = "Home Page"

        val startQuizBtn = findViewById<Button>(R.id.startQuizBtn)
        startQuizBtn.setOnClickListener {v -> startQuiz(v)}
    }

    // https://opentdb.com/api.php?amount=10&category=9&difficulty=easy&type=multiple
    private fun startQuiz(view: View) {
        Ion.with(this)
            .load("https://opentdb.com/api.php?amount=5&category=9&difficulty=easy&type=multiple")
            .asString()
            .setCallback {ex, result ->
                val quizPageIntent = Intent(this, QuizActivity::class.java)
                quizPageIntent.putExtra("quizData", result)
                quizPageIntent.putExtra("numOfQuestions", 5)
                startActivity(quizPageIntent)
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage_appbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = findViewById<View>(R.id.homePageAppBar)
        when (item.itemId) {
            R.id.refresh -> {
                val snackBar =
                    Snackbar.make(view, "Refresh Icon Was Clicked", Snackbar.LENGTH_LONG)
                snackBar.show()
                return true
            }
            R.id.actionLogOut -> {
                logOut()
                return true
            }
            R.id.actionSettings -> {
                settingsActivityShow()
                return true
            }
            R.id.actionPreviousResponses -> {
                previousResponsesActivityShow()
                return true
            }
            R.id.actionAchievements -> {
                achievementsActivityShow()
                return true
            }
            R.id.profile -> {
                profileActivityShow()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun logOut() {
        mAuth.signOut()
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.putExtra("loggedOff", true)
        startActivity(loginIntent)
    }

    private fun settingsActivityShow() {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }

    private fun previousResponsesActivityShow() {
        val previousResponsesIntent = Intent(this, PreviousResponsesActivity::class.java)
        startActivity(previousResponsesIntent)
    }

    private fun achievementsActivityShow() {
        val achievementsIntent = Intent(this, AchievementsActivity::class.java)
        startActivity(achievementsIntent)
    }

    private fun profileActivityShow() {
        val profileIntent = Intent(this, ProfileActivity::class.java)
        startActivity(profileIntent)
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()
    }


}