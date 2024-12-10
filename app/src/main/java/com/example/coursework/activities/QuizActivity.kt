package com.example.coursework.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {

    private var quizData = ""
    private var numOfQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val intentInfo = intent.extras
        if (intentInfo != null) {
            quizData = intentInfo.getString("quizData").toString()
            numOfQuestions = intentInfo.getInt("numOfQuestions")
        }

        val numOfQuestionsList = populateNumOfQuestionsList(quizData)

        val recyclerView = findViewById<View>(R.id.quizRecyclerView) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(this) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = QuestionRecyclerAdapter(numOfQuestionsList)
        recyclerView.adapter = mAdapter
    }


    private fun populateNumOfQuestionsList(quizData: String): ArrayList<Question> {
        val list = ArrayList<Question>()
        val quizJsonArray = JSONObject(quizData)
        val resultsArray = quizJsonArray.getJSONArray("results")

        for (i in 0 until resultsArray.length()) {
            val result = resultsArray.getJSONObject(i)

            val wrongAnswersJsonArray = result.getJSONArray("incorrect_answers")
            val type = result.getString("type")
            val difficulty = result.getString("difficulty")
            val category = result.getString("category")
            val question = result.getString("question")
            val correctAnswer = result.getString("correct_answer")

            val wrongAnswersList: MutableList<String> = mutableListOf()
            for (j in 0 until wrongAnswersJsonArray.length()) {
                wrongAnswersList.add(wrongAnswersJsonArray.getString(j))
            }
            var typeIsBoolean = false
            if (type == "boolean") {
                typeIsBoolean = true
            }

            val incorrectAnswersArray = wrongAnswersList.toTypedArray()

            val questionObject = Question(question,
                                    R.drawable.hourglass,
                                    difficulty,
                                    category,
                                    correctAnswer,
                                    incorrectAnswersArray,
                                    typeIsBoolean)
            list.add(questionObject)
            Log.d("RecyclerView", "Binding position: , question: $question")
        }
        return list
    }
}