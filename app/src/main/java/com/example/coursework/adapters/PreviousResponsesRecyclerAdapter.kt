package com.example.coursework.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Answer
import com.example.coursework.Quiz
import com.example.coursework.R
import com.example.coursework.activities.QuizActivity
import com.example.coursework.fragments.QuestionFragment
import com.example.coursework.fragments.QuizFragment
import com.google.android.material.snackbar.Snackbar

class PreviousResponsesRecyclerAdapter (
    private val quizArrayList: MutableList<Quiz>)
    : RecyclerView.Adapter<PreviousResponsesRecyclerAdapter.ViewHolder>() {

    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)

        return ViewHolder(v)
    }

    /*
   * Bind the data to the child views of the ViewHolder
   */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = quizArrayList[position]
        holder.txtMsg.text = info.quizName
    }

    /*
     * Get the maximum size of the
     */
    override fun getItemCount(): Int {
        return quizArrayList.size
    }

    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var txtMsg = itemView.findViewById<View>(R.id.firstLine) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val info = quizArrayList[adapterPosition]
            val bundle = Bundle()
            bundle.putString("quizName", info.quizName)
            bundle.putInt("numOfQuestions", info.numOfQuestions)
            bundle.putInt("score", info.score)

            val fragment = QuizFragment() // Replace with your fragment class
            fragment.arguments = bundle
            val fragmentManager = (v.context as AppCompatActivity).supportFragmentManager

            // Begin a transaction to replace the fragment
            fragmentManager.beginTransaction().apply {
                replace(R.id.previousResponsesFragmentContainer, fragment, "CURRENTLY_OPENED_FRAGMENT")
                addToBackStack(null)  // Ensures the fragment is added to the back stack
                commit()
            }
        }
    }
}
