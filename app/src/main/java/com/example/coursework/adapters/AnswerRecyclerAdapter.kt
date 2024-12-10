package com.example.coursework.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.Answer
import com.example.coursework.Question
import com.example.coursework.R
import com.example.coursework.fragments.MultipleChoiceQuestionFragment
import com.google.android.material.snackbar.Snackbar

class AnswerRecyclerAdapter (private val imageModelArrayList: MutableList<Answer>) : RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder>() {

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

        var imgView = itemView.findViewById<View>(R.id.icon) as ImageView
        var txtMsg = itemView.findViewById<View>(R.id.firstLine) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val info = imageModelArrayList[adapterPosition]
            if (txtMsg.text == info.correctAnswer) {
                val snackBar = Snackbar.make(v, "correct answer clicked", Snackbar.LENGTH_LONG)
                snackBar.show()
            } else {
                val snackBar = Snackbar.make(v, "wrong answer clicked", Snackbar.LENGTH_LONG)
                snackBar.show()
            }
        }
    }
}




//var intent = Intent(itemView.context, TeamDetail::class.java)
//itemView.context.startActivity(intent)