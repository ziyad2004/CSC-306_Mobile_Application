package com.example.coursework.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.coursework.R
import com.example.coursework.adapters.TabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val mToolBar = findViewById<Toolbar>(R.id.settingsAppBar)
        setSupportActionBar(mToolBar)

        supportActionBar?.title = "Settings"


        val tabLayout = findViewById<TabLayout>(R.id.settingsTabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.settingsPager)

        val tabTitles = resources.getStringArray(R.array.settings_tab_array)
        viewPager.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = tabTitles[0]
                1 -> tab.text = tabTitles[1]
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_appbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.returnToHomePage -> {
                homePageActivityShow()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun homePageActivityShow() {
        val homePageIntent = Intent(this, HomePageActivity::class.java)
        startActivity(homePageIntent)
    }

}