package com.example.coursework.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.koushikdutta.ion.Ion
import com.example.coursework.QuizSettings

class HomePageActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private var quizSettings = QuizSettings (
        1,
        "Any",
        "Any",
        "Any",
        "00:00"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val intentInfo = intent.extras
        if (intentInfo != null) {
            val quizResults = findViewById<TextView>(R.id.quizResultsTextView)
            quizResults.text = "Results: " + intentInfo.getInt("numOfCorrectAnswers") +
                    " out of " + intentInfo.getInt("numOfQuestions")
        }

        val view = findViewById<View>(R.id.mainView)
        setQuizSettings(view)

        val mToolBar = findViewById<Toolbar>(R.id.homePageAppBar)
        setSupportActionBar(mToolBar)

        supportActionBar?.title = "Home Page"

        val startQuizBtn = findViewById<Button>(R.id.startQuizBtn)
        startQuizBtn.setOnClickListener {_ -> startQuiz()}
    }

    private fun startQuiz() {
        val url = getUrl()
        Ion.with(this)
            .load(url)
            .asString()
            .setCallback {ex, result ->
                val quizPageIntent = Intent(this, QuizActivity::class.java)
                quizPageIntent.putExtra("quizData", result)
                quizPageIntent.putExtra("numOfQuestions", quizSettings.amount.toInt())
                startActivity(quizPageIntent)
            }
    }

    private fun getUrl(): String {
        var fullURL = "https://opentdb.com/api.php?amount="
        var amountQuery = "1"
        var categoryQuery = ""
        var difficultyQuery = ""
        var typeQuery = ""

        if (quizSettings.amount != amountQuery.toLong()) {
            amountQuery = quizSettings.amount.toString()
        }

        if (quizSettings.category != "Any") {
            val categoryNum = categories[quizSettings.category]
            categoryQuery = "&category=" + categoryNum.toString()
        }

        if (quizSettings.difficulty != "Any") {
            difficultyQuery = "&difficulty=" + quizSettings.difficulty.lowercase()
        }

        if (quizSettings.type != "Any") {
            typeQuery = "&type=" + quizSettings.type.lowercase()
        }

        return fullURL + amountQuery + categoryQuery + difficultyQuery + typeQuery;
    }

    private fun setQuizSettings(view: View) {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentSettingsRef = db.collection("users")
                .document(userId)
                .collection("quiz_settings")
                .document("settings")

            documentSettingsRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    quizSettings.amount = document.get("amount") as Long
                    quizSettings.type = document.get("type") as String
                    quizSettings.difficulty = document.get("difficulty") as String
                    quizSettings.category = document.get("category") as String
                    quizSettings.time = document.get("time") as String
                }
            }.addOnFailureListener { _ ->
                displayMsg(view, "Could Not Retrieve Quiz Settings")
            }
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

    private fun displayMsg(view: View, msg: String) {
        val sb = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    companion object {
        val categories: MutableMap<String, Int> = mutableMapOf(
            "General Knowledge" to 9,
            "Entertainment: Books" to 10,
            "Entertainment: Film" to 11,
            "Entertainment: Music" to 12,
            "Entertainment: Musicals & Theatres" to 13,
            "Entertainment: Television" to 14,
            "Entertainment: Video Games" to 15,
            "Entertainment: Board Games" to 16,
            "Science & Nature" to 17,
            "Science: Computers" to 18,
            "Science: Mathematics" to 19,
            "Mythology" to 20,
            "Sports" to 21,
            "Geography" to 22,
            "History" to 23,
            "Politics" to 24,
            "Art" to 25,
            "Celebrities" to 26,
            "Animals" to 27,
            "Vehicles" to 28,
            "Entertainment: Comics" to 29,
            "Science: Gadgets" to 30,
            "Entertainment: Japanese Anime & Manga" to 31,
            "Entertainment: Cartoon & Animations" to 32
        )
    }
}