package com.example.coursework.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Answer
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.AnswerRecyclerAdapter


class MultipleChoiceQuestionFragment : Fragment(){

    private var correctAnswer = ""
    private var incorrectAnswers: Array<String> = arrayOf()
    private var typeIsBoolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_multiple_choice_question, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questionRecyclerView = activity?.findViewById<RecyclerView>(R.id.quizRecyclerView)
        questionRecyclerView?.visibility = View.GONE

        val bundle = arguments
        correctAnswer = bundle?.getString("correctAnswer").toString()
        incorrectAnswers = bundle?.getStringArray("incorrectAnswers")!!
        typeIsBoolean = bundle.getBoolean("isBoolean")

        val numOfQuestionsList = populateQuestionChoiceList()

        val recyclerView = view.findViewById<View>(R.id.multipleChoiceRecycler) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(view.context) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = AnswerRecyclerAdapter(numOfQuestionsList)
        recyclerView.adapter = mAdapter
    }

    private fun populateQuestionChoiceList(): ArrayList<Answer> {
        val list = ArrayList<Answer>()
        val numOfAnswers = if (typeIsBoolean) {
            2
        } else {
            4
        }

        val answersArray = arrayOf(*incorrectAnswers, correctAnswer)
        val shuffledAnswersArray = answersArray.toList().shuffled()
        Log.i("Arrays", "Answers Array: $answersArray, Shuffled Array: $shuffledAnswersArray")

        for (i in 0 until 4) {
            val answer = Answer(correctAnswer, shuffledAnswersArray[i])
            list.add(answer)
        }
        return list
    }

    override fun onPause() {
        super.onPause()
        // Show the RecyclerView when leaving the fragment
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.quizRecyclerView)
        recyclerView?.visibility = View.VISIBLE
    }
}