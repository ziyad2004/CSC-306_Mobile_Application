package com.example.coursework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.coursework.R

class QuestionSettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var areSpinnersInitialised = true

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

        questionsTimeSpinner.onItemSelectedListener = this
        numOfQuestionsSpinner.onItemSelectedListener = this
        categoriesSpinner.onItemSelectedListener = this
        difficultySpinner.onItemSelectedListener = this
        typeOfQuestionsSpinner.onItemSelectedListener = this
        areSpinnersInitialised = true

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!areSpinnersInitialised) return
        when (parent?.id) {
            R.id.numOfQuestionsSpinner -> {
                val selectedItem = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Spinner 1 selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            R.id.categoriesSpinner -> {
                val selectedItem = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Spinner 2 selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            R.id.difficultySpinner -> {
                val selectedItem = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Spinner 3 selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            R.id.typeOfQuestionsSpinner -> {
                val selectedItem = parent.getItemAtPosition(position)
                //Toast.makeText(this, "Spinner 4 selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}