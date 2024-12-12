package com.example.coursework

import android.media.Image

/*
 * Data model class to store logos and team names from F1
 */

data class Question(val id: Int,
                    val question: String,
                    val difficulty: String,
                    val category: String,
                    val correctAnswer: String,
                    var incorrectAnswers: Array<String>,
                    val type: String)

