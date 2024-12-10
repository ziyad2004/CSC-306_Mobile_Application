package com.example.coursework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter


class BooleanQuestionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_multiple_choice_question, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questionRecyclerView = activity?.findViewById<RecyclerView>(R.id.quizRecyclerView)
        questionRecyclerView?.visibility = View.GONE

        //val numOfQuestionsList = populateQuestionChoiceList()

        val recyclerView = view.findViewById<View>(R.id.booleanChoiceRecycler) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(view.context) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        //val mAdapter = QuestionRecyclerAdapter(numOfQuestionsList)
        //recyclerView.adapter = mAdapter

    }
/*
    private fun populateQuestionChoiceList(): ArrayList<Question> {
        val list = ArrayList<Question>()
        val trueModel = Question("True")
        val falseModel = Question("False")
        list.add(trueModel)
        list.add(falseModel)
        return list
    }

 */


    override fun onPause() {
        super.onPause()
        // Show the RecyclerView when leaving the fragment
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.quizRecyclerView)
        recyclerView?.visibility = View.VISIBLE
    }
}