package com.example.coursework

import android.util.Log

object QuizData {
    private var quizs = ArrayList<Quiz>()

    fun addQuiz(quiz: Quiz) {
        quizs.add(quiz)
    }

    fun getQuizData(): ArrayList<Quiz> {
        return this.quizs
    }

    fun getQuiz(quizName: String): Quiz {
        val id = quizName.drop(4).toInt() - 1
        return quizs[id]
    }

    fun hasQuiz(quizName: String): Boolean {
        for (quiz in quizs) {
            if (quiz.quizName == quizName) {
                return true
            }
        }
        return false
    }
}