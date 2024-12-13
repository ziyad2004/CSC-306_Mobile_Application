package com.example.coursework.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.QuizData
import com.example.coursework.R
import com.example.coursework.adapters.QuizAnswersRecyclerAdapter


class QuizFragment : Fragment(){

    private var quizName = ""
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_quiz, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val responsesRecyclerView = activity?.findViewById<RecyclerView>(R.id.previousResponsesRecyclerView)
        responsesRecyclerView?.visibility = View.GONE

        val bundle = arguments
        quizName = bundle?.getString("quizName").toString()
        val numOfQuestions = bundle?.getInt("numOfQuestions").toString()
        val score = bundle?.getInt("score").toString()

        val quizAnswersList = populateQuizAnswerList()

        val quizCategoryTextView = view.findViewById<View>(R.id.quizCategory) as TextView
        quizCategoryTextView.text = getString(R.string.category, quizAnswersList[0].category)

        val quizDifficultyTextView = view.findViewById<View>(R.id.quizDifficulty) as TextView
        quizDifficultyTextView.text = getString(R.string.difficulty, quizAnswersList[0].difficulty)

        val quizTypeTextView = view.findViewById<View>(R.id.quizType) as TextView
        quizTypeTextView.text = getString(R.string.type, quizAnswersList[0].type)

        val quizScoreTextView = view.findViewById<View>(R.id.quizScore) as TextView
        quizScoreTextView.text = getString(R.string.score, score, numOfQuestions)


        val recyclerView = view.findViewById<View>(R.id.previousQuestionsRecyclerView) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(view.context) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = this.context?.let { QuizAnswersRecyclerAdapter(quizAnswersList) }
        recyclerView.adapter = mAdapter
    }

    private fun populateQuizAnswerList(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()
        val quiz = QuizData.getQuiz(quizName)
        val questions = quiz.questions

        for (question in questions) {
            val id = question.id
            val ques = question.question
            val difficulty = question.difficulty
            val category = question.category
            val correctAnswer = question.correctAnswer
            val chosenAnswer = question.incorrectAnswers[0]
            val type = question.type

            questionsList.add(Question(
                id = id,
                question = ques,
                difficulty = difficulty,
                category = category,
                correctAnswer = correctAnswer,
                incorrectAnswers = arrayOf(chosenAnswer),
                type = type
            ))
        }

        return questionsList
    }
    

    override fun onPause() {
        super.onPause()
        // Show the RecyclerView when leaving the fragment
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.previousResponsesRecyclerView)
        recyclerView?.visibility = View.VISIBLE
    }


    
}