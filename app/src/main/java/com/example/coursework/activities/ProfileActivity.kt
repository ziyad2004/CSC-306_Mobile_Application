package com.example.coursework.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coursework.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private lateinit var  quizsCompletedTextView : TextView
    private lateinit var  easyQuizsCompletedTextView : TextView
    private lateinit var  mediumQuizsCompletedTextView : TextView
    private lateinit var  hardQuizsCompletedTextView : TextView
    private lateinit var  questionsAnsweredTextView : TextView
    private lateinit var  scoreTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userNameTxtView = findViewById<TextView>(R.id.userNameTxtView)
        userNameTxtView.text = "User Name: ${mAuth.currentUser?.email}"

        quizsCompletedTextView = findViewById(R.id.quizsCompletedTextView)
        easyQuizsCompletedTextView = findViewById(R.id.easyQuizsCompletedTextView)
        mediumQuizsCompletedTextView = findViewById(R.id.mediumQuizsCompletedTextView)
        hardQuizsCompletedTextView = findViewById(R.id.hardQuizsCompletedTextView)
        questionsAnsweredTextView = findViewById(R.id.questionsAnsweredTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        setStats()
    }

    private fun setStats() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentQuizStatsRef = db.collection("users")
                .document(userId)
                .collection("quiz_stats")
                .document("stats")

            documentQuizStatsRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    quizsCompletedTextView.text =
                        "Number of Quiz's Completed: ${(document.get("quizsCompleted") as Long).toString()}"
                    hardQuizsCompletedTextView.text =
                        "Number of Hard Quiz's Completed: ${(document.get("hardQuizsCompleted") as Long).toString()}"
                    easyQuizsCompletedTextView.text =
                        "Number of Easy Quiz's Completed: ${(document.get("easyQuizsCompleted") as Long).toString()}"
                    mediumQuizsCompletedTextView.text =
                        "Number of Medium Quiz's Completed: ${(document.get("mediumQuizsCompleted") as Long).toString()}"
                    questionsAnsweredTextView.text =
                        "Number of Questions Answered: ${(document.get("numOfQuestionsAnswered") as Long).toString()}"
                    scoreTextView.text =
                        "Number of Correctly Answered Questions: ${(document.get("numOfCorrectlyChosenAnswers") as Long).toString()}"
                }
            }
        }
    }
}