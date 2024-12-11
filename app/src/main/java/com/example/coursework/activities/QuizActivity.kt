package com.example.coursework.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {

    private var quizData = ""
    private var numOfQuestions = 0
    private var correctAndChosenAnswers: MutableMap<String, String> = mutableMapOf()
    private var intentFilter: IntentFilter? = null
    private var numOfCorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        intentFilter = IntentFilter()
        intentFilter!!.addAction(BROADCAST_ANSWER_ACTION)


        val submitBtn = findViewById<Button>(R.id.submitQuizBtn)
        submitBtn.setOnClickListener { _ -> submitQuiz() }


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

            val questionObject = Question(i,
                                    question,
                                    difficulty,
                                    category,
                                    correctAnswer,
                                    incorrectAnswersArray,
                                    typeIsBoolean)
            list.add(questionObject)
            correctAndChosenAnswers["$i$correctAnswer"] = ""
            Log.d("RecyclerView", "Binding position: , question: $question")
        }
        return list
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BROADCAST_ANSWER_ACTION -> {
                    addChosenAnswer(intent.getStringExtra("questionId"),
                        intent.getStringExtra("chosenAnswer"))
                }
            }
        }
    }

    private fun addChosenAnswer(questionId: String? = "-1", chosenAnswer: String?) {
        var answerChosen = ""
        if (chosenAnswer != null) {
            answerChosen = chosenAnswer
        }
        for ((key, _) in correctAndChosenAnswers) {
            if (questionId?.let { key.startsWith(it) } == true) {
                correctAndChosenAnswers[key] = answerChosen
            }
        }
        Log.d("chosenAnswersArray", "$correctAndChosenAnswers")
    }

    public override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter, RECEIVER_NOT_EXPORTED)
    }

    public override fun onPause() {
        registerReceiver(receiver, intentFilter, RECEIVER_NOT_EXPORTED)
        super.onPause()
    }

    private fun submitQuiz() {
        for ((key, value) in correctAndChosenAnswers) {
            val correctAnswer = key.substring(1)
            if (correctAnswer == value) {
                numOfCorrectAnswers++
            }
        }
        val homePageIntent = Intent(this, HomePageActivity::class.java)
        homePageIntent.putExtra("numOfCorrectAnswers", numOfCorrectAnswers)
        homePageIntent.putExtra("numOfQuestions", numOfQuestions)
        startActivity(homePageIntent)
    }

    companion object {
        const val BROADCAST_ANSWER_ACTION = "com.example.broadcast.answer"
    }
}