package com.example.coursework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter
import com.google.android.material.snackbar.Snackbar

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        Log.e("MainActivity", "Error starting SecondActivity")

        val mToolBar = findViewById<Toolbar>(R.id.homePageAppBar)
        setSupportActionBar(mToolBar)

        supportActionBar?.title = "Home Page"


        val numOfQuestionsList = populateNumOfQuestionsList()

        val recyclerView = findViewById<View>(R.id.homePageRecyclerView) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(this) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = QuestionRecyclerAdapter(numOfQuestionsList)
        recyclerView.adapter = mAdapter

    }


    private fun populateNumOfQuestionsList(): ArrayList<Question> {
        val list = ArrayList<Question>()
        for (i in 1 .. 10) {
            val settingsOptionModel = Question("question description", R.drawable.hourglass)
            list.add(settingsOptionModel)
        }
        return list
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
            R.id.actionLogoff -> {
                loginActivityShow()
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


    private fun loginActivityShow() {
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