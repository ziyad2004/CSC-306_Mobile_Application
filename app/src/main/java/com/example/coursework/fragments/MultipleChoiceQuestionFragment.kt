package com.example.coursework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter

class MultipleChoiceQuestionFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_multiple_choice_question, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeRecyclerView = activity?.findViewById<RecyclerView>(R.id.homePageRecyclerView)
        homeRecyclerView?.visibility = View.GONE

        val numOfQuestionsList = populateQuestionChoiceList()

        val recyclerView = view.findViewById<View>(R.id.multipleChoiceRecycler) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(view.context) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = QuestionRecyclerAdapter(numOfQuestionsList)
        recyclerView.adapter = mAdapter

    }

    private fun populateQuestionChoiceList(): ArrayList<Question> {
        val list = ArrayList<Question>()
        for (i in 1 .. 10) {
            val settingsOptionModel = Question("question choice $i", R.drawable.checkmark_image)
            list.add(settingsOptionModel)
        }
        return list
    }


    override fun onPause() {
        super.onPause()
        // Show the RecyclerView when leaving the fragment
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.homePageRecyclerView)
        recyclerView?.visibility = View.VISIBLE
    }

}