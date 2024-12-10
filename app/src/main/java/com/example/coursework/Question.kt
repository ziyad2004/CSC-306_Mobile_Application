package com.example.coursework

import android.media.Image

/*
 * Data model class to store logos and team names from F1
 */

data class Question(val question: String,
                    val image: Int? = null,
                    val difficulty: String,
                    val category: String,
                    val correctAnswer: String,
                    val incorrectAnswers: Array<String>,
                    val isBoolean: Boolean)

