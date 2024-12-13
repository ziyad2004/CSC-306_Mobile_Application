package com.example.coursework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.Question

class QuizAnswersRecyclerAdapter (
    private val questionArrayList: MutableList<Question>)
    : RecyclerView.Adapter<QuizAnswersRecyclerAdapter.ViewHolder>() {

    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.previous_question_row_layout, parent, false)

        return ViewHolder(v)
    }

    /*
   * Bind the data to the child views of the ViewHolder
   */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = questionArrayList[position]
        holder.questionTxt.text = info.question
        holder.correctAnswerTxt.text = "Correct Answer: ${info.correctAnswer}"
        holder.chosenAnswerTxt.text = "Chosen Answer: ${info.incorrectAnswers[0]}"
    }

    /*
     * Get the maximum size of the
     */
    override fun getItemCount(): Int {
        return questionArrayList.size
    }

    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionTxt = itemView.findViewById<View>(R.id.questionTextView) as TextView
        var correctAnswerTxt = itemView.findViewById<View>(R.id.correctAnswerTextView) as TextView
        var chosenAnswerTxt = itemView.findViewById<View>(R.id.chosenAnswerTextView) as TextView
    }
}
