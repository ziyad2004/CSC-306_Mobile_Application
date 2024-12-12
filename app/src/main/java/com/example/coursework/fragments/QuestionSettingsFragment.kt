package com.example.coursework.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.coursework.QuizSettings
import com.example.coursework.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class QuestionSettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var areSpinnersInitialised = false
    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private var quizSettings = QuizSettings (
        1,
        "Any",
        "Any",
        "Any",
        "00:00"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_question_settings, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val numOfQuestionsSpinner = view.findViewById<Spinner>(R.id.numOfQuestionsSpinner)
        ArrayAdapter.createFromResource(view.context,
            R.array.num_of_questions_array,
            R.layout.spinner_layout
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout)
                numOfQuestionsSpinner.adapter = adapter
            }

        val categoriesSpinner = view.findViewById<Spinner>(R.id.categoriesSpinner)
        ArrayAdapter.createFromResource(view.context,
            R.array.categories_array,
            R.layout.spinner_layout
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout)
                categoriesSpinner.adapter = adapter
            }

        val difficultySpinner = view.findViewById<Spinner>(R.id.difficultySpinner)
        ArrayAdapter.createFromResource(view.context,
            R.array.difficulty_array,
            R.layout.spinner_layout
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout)
                difficultySpinner.adapter = adapter
            }

        val typeOfQuestionsSpinner = view.findViewById<Spinner>(R.id.typeOfQuestionsSpinner)
        ArrayAdapter.createFromResource(view.context,
            R.array.type_of_questions_array,
            R.layout.spinner_layout
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout)
                typeOfQuestionsSpinner.adapter = adapter
            }

        val questionsTimeSpinner = view.findViewById<Spinner>(R.id.questionTimeSpinner)
        ArrayAdapter.createFromResource(view.context,
            R.array.question_time_array,
            R.layout.spinner_layout
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout)
                questionsTimeSpinner.adapter = adapter
            }

        numOfQuestionsSpinner.onItemSelectedListener = this
        categoriesSpinner.onItemSelectedListener = this
        difficultySpinner.onItemSelectedListener = this
        typeOfQuestionsSpinner.onItemSelectedListener = this
        questionsTimeSpinner.onItemSelectedListener = this
        areSpinnersInitialised = true


        getQuizSettings(numOfQuestionsSpinner,
            categoriesSpinner,
            difficultySpinner,
            typeOfQuestionsSpinner,
            questionsTimeSpinner,
            view)

        val saveSettingsBtn = view.findViewById<Button>(R.id.saveSettingsBtn)
        saveSettingsBtn.setOnClickListener { _ -> saveSettings(view)}

        val clearSettingsBtn = view.findViewById<Button>(R.id.clearSettingsBtn)
        clearSettingsBtn.setOnClickListener { _ -> clearSettings(questionsTimeSpinner,
                                                                numOfQuestionsSpinner,
                                                                categoriesSpinner,
                                                                difficultySpinner,
                                                                typeOfQuestionsSpinner)}
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!areSpinnersInitialised) return
        when (parent?.id) {
            R.id.numOfQuestionsSpinner -> {
                val selectedItem = parent.getItemAtPosition(position) as String
                quizSettings.amount = selectedItem.toLong()
                if (view != null) {
                    displayMsg(view, "Number of Questions Selected: $selectedItem")
                }
            }

            R.id.categoriesSpinner -> {
                val selectedItem = parent.getItemAtPosition(position) as String
                quizSettings.category = selectedItem
                if (view != null) {
                    displayMsg(view, "Category Selected: $selectedItem")
                }
            }

            R.id.difficultySpinner -> {
                val selectedItem = parent.getItemAtPosition(position) as String
                quizSettings.difficulty = selectedItem
                if (view != null) {
                    displayMsg(view, "Difficulty Selected: $selectedItem")
                }
            }

            R.id.typeOfQuestionsSpinner -> {
                val selectedItem = parent.getItemAtPosition(position) as String

                if (selectedItem == "True/False") {
                    quizSettings.type = "boolean"
                } else if (selectedItem == "Multiple Choice") {
                    quizSettings.type = "multiple"
                } else {
                    quizSettings.type = "Any"
                }

                if (view != null) {
                    displayMsg(view, "Type of Questions Selected: $selectedItem")
                }
            }

            R.id.questionTimeSpinner -> {
                val selectedItem = parent.getItemAtPosition(position) as String
                quizSettings.time = selectedItem
                if (view != null) {
                    displayMsg(view, "$selectedItem Time Selected")
                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun saveSettings(view: View) {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val quizSettingsMap = mapOf(
                "amount" to quizSettings.amount,
                "type" to quizSettings.type,
                "difficulty" to quizSettings.difficulty,
                "category" to quizSettings.category,
                "time" to quizSettings.time
            )

            db.collection("users")
                .document(userId)
                .collection("quiz_settings")
                .document("settings")
                .set(quizSettingsMap, SetOptions.merge())
                .addOnSuccessListener { _ ->
                    displayMsg(view, "Settings Saved")
            }
                .addOnFailureListener { _ ->
                    displayMsg(view, "Error: Settings Not Saved")
                }
        }
    }

    private fun clearSettings(numOfQuestionsSpinner: Spinner,
                              categoriesSpinner: Spinner,
                              difficultySpinner: Spinner,
                              typeOfQuestionsSpinner: Spinner,
                              questionsTimeSpinner: Spinner) {
        quizSettings.amount = 1
        quizSettings.type = "Any"
        quizSettings.difficulty = "Any"
        quizSettings.category = "Any"
        quizSettings.time = "00:00"

        numOfQuestionsSpinner.setSelection(0)
        categoriesSpinner.setSelection(0)
        difficultySpinner.setSelection(0)
        typeOfQuestionsSpinner.setSelection(0)
        questionsTimeSpinner.setSelection(0)
    }

    private fun displayMsg(view: View, msg: String) {
        val sb = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    private fun getQuizSettings(numOfQuestionsSpinner: Spinner,
                            categoriesSpinner: Spinner,
                            difficultySpinner: Spinner,
                            typeOfQuestionsSpinner: Spinner,
                            questionsTimeSpinner: Spinner,
                            view: View) {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid

            val documentSettingsRef = db.collection("users")
                .document(userId)
                .collection("quiz_settings")
                .document("settings")

            documentSettingsRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    quizSettings.amount = document.get("amount") as Long
                    quizSettings.type = document.get("type") as String
                    quizSettings.difficulty = document.get("difficulty") as String
                    quizSettings.category = document.get("category") as String
                    quizSettings.time = document.get("time") as String

                    setSpinners(numOfQuestionsSpinner,
                        categoriesSpinner,
                        difficultySpinner,
                        typeOfQuestionsSpinner,
                        questionsTimeSpinner)
                }
            }.addOnFailureListener { _ ->
                displayMsg(view, "Could Not Retrieve Quiz Settings")
            }

        }
    }

    private fun setSpinners(numOfQuestionsSpinner: Spinner,
                            categoriesSpinner: Spinner,
                            difficultySpinner: Spinner,
                            typeOfQuestionsSpinner: Spinner,
                            questionsTimeSpinner: Spinner) {

        numOfQuestionsSpinner.setSelection(quizSettings.amount.toInt() - 1)

        val categoriesSpinnerPos = categories[quizSettings.category]?.minus(8)

        if (categoriesSpinnerPos != null) {
            categoriesSpinner.setSelection(categoriesSpinnerPos)
        }

        difficultySpinner.setSelection(
            when (quizSettings.difficulty) {
                "Any" -> 0
                "Easy" -> 1
                "Medium" -> 2
                "Hard" -> 3
                else -> 0
            }
        )

        typeOfQuestionsSpinner.setSelection(
            when (quizSettings.type) {
                "Any" -> 0
                "multiple" -> 1
                else -> 2
            }
        )

        if (quizSettings.time.startsWith("0")) {
            questionsTimeSpinner.setSelection(quizSettings.time[1].toString().toInt())
        } else {
            questionsTimeSpinner.setSelection(quizSettings.time.take(2).toInt())
        }
    }

    companion object {
        val categories: MutableMap<String, Int> = mutableMapOf(
            "Any" to 8,
            "General Knowledge" to 9,
            "Entertainment: Books" to 10,
            "Entertainment: Film" to 11,
            "Entertainment: Music" to 12,
            "Entertainment: Musicals & Theatres" to 13,
            "Entertainment: Television" to 14,
            "Entertainment: Video Games" to 15,
            "Entertainment: Board Games" to 16,
            "Science & Nature" to 17,
            "Science: Computers" to 18,
            "Science: Mathematics" to 19,
            "Mythology" to 20,
            "Sports" to 21,
            "Geography" to 22,
            "History" to 23,
            "Politics" to 24,
            "Art" to 25,
            "Celebrities" to 26,
            "Animals" to 27,
            "Vehicles" to 28,
            "Entertainment: Comics" to 29,
            "Science: Gadgets" to 30,
            "Entertainment: Japanese Anime & Manga" to 31,
            "Entertainment: Cartoon & Animations" to 32
        )
    }
}
