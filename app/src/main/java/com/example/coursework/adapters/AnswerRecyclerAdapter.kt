package com.example.coursework.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Answer
import com.example.coursework.R
import com.example.coursework.activities.QuizActivity
import com.example.coursework.fragments.QuestionFragment
import com.google.android.material.snackbar.Snackbar

class AnswerRecyclerAdapter (
    private val imageModelArrayList: MutableList<Answer>,
    private val fragment: QuestionFragment,
    private val context: Context)
    : RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder>() {

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
        val info = imageModelArrayList[position]
        holder.txtMsg.text = info.displayedAnswer
    }

    /*
     * Get the maximum size of the
     */
    override fun getItemCount(): Int {
        return imageModelArrayList.size
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
            val info = imageModelArrayList[adapterPosition]
            val broadcastIntent = Intent()
            broadcastIntent.action = QuizActivity.BROADCAST_ANSWER_ACTION
            broadcastIntent.putExtra("questionId", fragment.getQuestionId().toString())
            broadcastIntent.putExtra("chosenAnswer", info.displayedAnswer)
            context.sendBroadcast(broadcastIntent)

            val snackBar = Snackbar.make(v, "${info.displayedAnswer} clicked", Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }
}
