package com.gods.lambs.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gods.lambs.activity.QuizActivity
import com.gods.lambs.dataClass.QuizTitle
import com.gods.lambs.databinding.QuizItemRecyclerRowBinding

class QuizTitleAdapter (private val quizModeTitle : List<QuizTitle>) :
    RecyclerView.Adapter<QuizTitleAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: QuizTitle){
            // bind all views

            binding.apply {
                quizSubtitleText.text = model.subtitle
//                quizTimeText.text = model.time
                quizTitleText.text=model.title
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
//                    QuizActivity.questionModelList = model.questionList
//                    QuizActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModeTitle.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModeTitle[position])
    }
}