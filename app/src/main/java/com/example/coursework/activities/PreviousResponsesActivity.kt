package com.example.coursework.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Question
import com.example.coursework.Quiz
import com.example.coursework.QuizData
import com.example.coursework.R
import com.example.coursework.adapters.PreviousResponsesRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class PreviousResponsesActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private val quizList = ArrayList<Quiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_responses)

        populatePreviousResponsesList()
    }

    private fun populatePreviousResponsesList() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentQuizRef = db.collection("users")
                .document(userId)
                .collection("quizzes")

            documentQuizRef.get().addOnSuccessListener { documents ->
                if (!(documents.isEmpty)) {
                    for (document in documents.documents) {
                        quizList.add(createQuizObject(document))
                    }

                    val recyclerView = findViewById<View>(R.id.previousResponsesRecyclerView) as RecyclerView // Bind to the recyclerview in the layout
                    val layoutManager = LinearLayoutManager(this) // Get the layout manager
                    recyclerView.layoutManager = layoutManager

                    val mAdapter = PreviousResponsesRecyclerAdapter(quizList)
                    recyclerView.adapter = mAdapter
                }
            }
        }
    }

    private fun createQuizObject(document: DocumentSnapshot): Quiz {
        val quizQuestionsArray = ArrayList<Question>()
        val questions = document.get("questions") as? List<Map<String, Any>>
        val quizName = document.id
        val quizType = document.get("type") as String
        val quizDifficulty = document.get("difficulty") as String
        val quizCategory = document.get("category") as String
        val numOfQuestions = document.get("numOfQuestions") as Long
        val score = document.get("score") as Long

        if (questions != null) {
            for (question in questions) {
                val id = question["id"] as Long
                val ques = question["question"] as String
                val correctAnswer = question["correct_answer"] as String
                val chosenAnswer = question["chosen_answer"] as String

                val questionObject = Question (
                    id = id.toInt(),
                    question = ques,
                    difficulty = quizDifficulty,
                    category = quizCategory,
                    correctAnswer = correctAnswer,
                    incorrectAnswers = arrayOf(chosenAnswer),
                    type = quizType
                )
                quizQuestionsArray.add(questionObject)
            }
        }

        if (!(QuizData.hasQuiz(quizName))) {
            QuizData.addQuiz(Quiz(quizName, numOfQuestions.toInt(), score.toInt(), quizQuestionsArray))
        }
        return Quiz (quizName, numOfQuestions.toInt(), score.toInt(), quizQuestionsArray)
    }

}

