package com.example.coursework.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.QuizSettings
import com.example.coursework.R
import com.example.coursework.adapters.QuestionRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {

    private var quizData = ""
    private var numOfQuestions = 0
    private var correctAndChosenAnswers: MutableMap<String, String> = mutableMapOf()
    private var quizResults: MutableList<Question> = mutableListOf()
    private var intentFilter: IntentFilter? = null
    private var numOfCorrectAnswers = 0
    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private var lastQuizId = 0
    private var lastQuizIdRetrieved = false
    private val quizStatsMap = mutableMapOf (
        "quizsCompleted" to 0,
        "hardQuizsCompleted" to 0,
        "easyQuizsCompleted" to 0,
        "mediumQuizsCompleted" to 0,
        "numOfQuestionsAnswered" to 0,
        "numOfCorrectlyChosenAnswers" to 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        intentFilter = IntentFilter()
        intentFilter!!.addAction(BROADCAST_ANSWER_ACTION)

        getLastQuizId()
        getUserQuizStats()

        val submitBtn = findViewById<Button>(R.id.submitQuizBtn)
        submitBtn.setOnClickListener { _ -> submitQuiz() }


        val intentInfo = intent.extras
        if (intentInfo != null) {
            quizData = intentInfo.getString("quizData").toString()
            numOfQuestions = intentInfo.getInt("numOfQuestions")
        }

        val numOfQuestionsList = populateQuestionsList(quizData)

        val recyclerView = findViewById<View>(R.id.quizRecyclerView) as RecyclerView // Bind to the recyclerview in the layout
        val layoutManager = LinearLayoutManager(this) // Get the layout manager
        recyclerView.layoutManager = layoutManager

        val mAdapter = QuestionRecyclerAdapter(numOfQuestionsList)
        recyclerView.adapter = mAdapter
    }


    private fun populateQuestionsList(quizData: String): ArrayList<Question> {
        val list = ArrayList<Question>()
        val quizJsonArray = JSONObject(quizData)
        val resultsArray = quizJsonArray.getJSONArray("results")

        for (i in 0 until resultsArray.length()) {
            val result = resultsArray.getJSONObject(i)

            val wrongAnswersJsonArray = result.getJSONArray("incorrect_answers")
            val type = result.getString("type")
            val difficulty = result.getString("difficulty")
            val category = Html.fromHtml(result.getString("category"),
                Html.FROM_HTML_MODE_LEGACY).toString()
            val question = Html.fromHtml(result.getString("question"),
                Html.FROM_HTML_MODE_LEGACY).toString()
            val correctAnswer = Html.fromHtml(result.getString("correct_answer"),
                Html.FROM_HTML_MODE_LEGACY).toString()

            val wrongAnswersList: MutableList<String> = mutableListOf()
            for (j in 0 until wrongAnswersJsonArray.length()) {
                wrongAnswersList.add(Html.fromHtml(wrongAnswersJsonArray.getString(j),
                    Html.FROM_HTML_MODE_LEGACY).toString())
            }


            val incorrectAnswersArray = wrongAnswersList.toTypedArray()

            val questionObject = Question(i,
                                    question,
                                    difficulty,
                                    category,
                                    correctAnswer,
                                    incorrectAnswersArray,
                                    type)
            list.add(questionObject)
            quizResults.add(questionObject)
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
        if (lastQuizIdRetrieved) {
            var i = 0
            for ((key, value) in correctAndChosenAnswers) {
                val correctAnswer = key.substring(1)
                if (correctAnswer == value) {
                    numOfCorrectAnswers++
                }
                quizResults[i].incorrectAnswers = arrayOf(value)
                i++
            }
            saveQuiz()

            val homePageIntent = Intent(this, HomePageActivity::class.java)
            homePageIntent.putExtra("numOfCorrectAnswers", numOfCorrectAnswers)
            homePageIntent.putExtra("numOfQuestions", numOfQuestions)
            startActivity(homePageIntent)
        }
    }

    private fun saveQuiz() {
        val user = mAuth.currentUser
        val quizResultsMap = hashMapOf(
            "numOfQuestions" to numOfQuestions,
            "score" to numOfCorrectAnswers,
            "difficulty" to quizResults[0].difficulty,
            "type" to quizResults[0].type,
            "category" to quizResults[0].category,
            "questions" to mutableListOf<Map<String, Any>>()
        )

        if (user != null) {
            val userId = user.uid
            for (question in quizResults) {
                val questionResultMap = mapOf(
                    "id" to question.id,
                    "question" to question.question,
                    "correct_answer" to question.correctAnswer,
                    "chosen_answer" to question.incorrectAnswers[0],
                )

                (quizResultsMap["questions"] as MutableList<Map<String, Any>>).add(questionResultMap)
            }

            val quizId = "quiz${(lastQuizId + 1).toString().padStart(4, '0')}"
            db.collection("users")
                .document(userId)
                .collection("quizzes")
                .document(quizId)
                .set(quizResultsMap)
                .addOnSuccessListener { _ ->
                    calculateUserQuizStats(quizResultsMap)
                }
        }
    }

    private fun calculateUserQuizStats(quizResults: HashMap<String, Any>) {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            quizStatsMap["quizsCompleted"] = quizStatsMap["quizsCompleted"]!! + 1

            if (quizResults["difficulty"] == "hard") {
                quizStatsMap["hardQuizsCompleted"] = quizStatsMap["hardQuizsCompleted"]!! + 1
            } else if (quizResults["difficulty"] == "medium") {
                quizStatsMap["mediumQuizsCompleted"] = quizStatsMap["mediumQuizsCompleted"]!! + 1
            } else if (quizResults["difficulty"] == "easy") {
                quizStatsMap["easyQuizsCompleted"] = quizStatsMap["easyQuizsCompleted"]!! + 1
            }

            quizStatsMap["numOfQuestionsAnswered"] =
                quizStatsMap["numOfQuestionsAnswered"]!! + quizResults["numOfQuestions"] as Int

            quizStatsMap["numOfCorrectlyChosenAnswers"] =
                quizStatsMap["numOfCorrectlyChosenAnswers"]!! + quizResults["score"] as Int

            db.collection("users")
                .document(userId)
                .collection("quiz_stats")
                .document("stats")
                .set(quizStatsMap, SetOptions.merge())
        }
    }

    private fun getUserQuizStats() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentQuizStatsRef = db.collection("users")
                .document(userId)
                .collection("quiz_stats")
                .document("stats")

            documentQuizStatsRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    quizStatsMap["quizsCompleted"] = (document.get("quizsCompleted") as Long).toInt()
                    quizStatsMap["hardQuizsCompleted"] = (document.get("hardQuizsCompleted") as Long).toInt()
                    quizStatsMap["easyQuizsCompleted"] = (document.get("easyQuizsCompleted") as Long).toInt()
                    quizStatsMap["mediumQuizsCompleted"] = (document.get("mediumQuizsCompleted") as Long).toInt()
                    quizStatsMap["numOfQuestionsAnswered"] = (document.get("numOfQuestionsAnswered") as Long).toInt()
                    quizStatsMap["numOfCorrectlyChosenAnswers"] = (document.get("numOfCorrectlyChosenAnswers") as Long).toInt()
                }
            }
        }
    }

    private fun getLastQuizId() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentQuizRef = db.collection("users")
                .document(userId)
                .collection("quizzes")

            documentQuizRef.get().addOnSuccessListener { documents ->
                if (!(documents.isEmpty)) {
                    for (document in documents.documents) {
                        val documentId = document.id
                        val lastIdNumber = documentId.drop(4).toIntOrNull()
                        lastIdNumber?.let {
                            lastQuizId = it
                        }
                    }
                }
                lastQuizIdRetrieved = true
            }
        }
    }


    companion object {
        const val BROADCAST_ANSWER_ACTION = "com.example.broadcast.answer"
    }
}