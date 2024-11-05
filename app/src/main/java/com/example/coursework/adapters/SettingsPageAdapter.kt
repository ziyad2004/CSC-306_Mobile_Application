package com.example.coursework.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coursework.fragments.QuestionSettingsFragment
import com.example.coursework.fragments.UserSettingsFragment

class TabAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(index: Int): Fragment {
        when (index) {
            0 -> return QuestionSettingsFragment()
            1 -> return UserSettingsFragment()
        }
        return QuestionSettingsFragment()
    }

    // get item count - equal to number of tabs
    override fun getItemCount(): Int
    {
        return 2
    }
}