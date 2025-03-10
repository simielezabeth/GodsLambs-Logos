package com.gods.lambs.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gods.lambs.R
import com.gods.lambs.dataClass.QuestionModel
import com.gods.lambs.databinding.ActivityQuizBinding
import com.gods.lambs.databinding.ScoreDialogBinding
import com.gods.lambs.databinding.ScoreDialogBinding.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator

class QuizActivity : AppCompatActivity() ,View.OnClickListener{
    companion object {
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        loadQuestions()
        startTimer()
    }

    private fun startTimer(){
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis,1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished /1000
                val minutes = seconds/60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes,remainingSeconds)

            }

            override fun onFinish() {
                //Finish the quiz
            }

        }.start()
    }

    private fun loadQuestions(){
        selectedAnswer = ""
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                ( currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100 ).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))

            btn0.setTextColor(getColor(R.color.colorAccent))
            btn1.setTextColor(getColor(R.color.colorAccent))
            btn2.setTextColor(getColor(R.color.colorAccent))
            btn3.setTextColor(getColor(R.color.colorAccent))
        }

        val clickedBtn = view as Button
        if(clickedBtn.id==R.id.next_btn){
            //next button is clicked
            if(selectedAnswer.isEmpty()){
                Toast.makeText(applicationContext,"Please select answer to continue",Toast.LENGTH_SHORT).show()
                return;
            }
            if(selectedAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of quiz",score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        }else{
            //options button is clicked
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.colorAccent))
            clickedBtn.setTextColor(Color.WHITE)
        }
    }

    private fun finishQuiz() {
        showScoreDialog(this, score, questionModelList.size) {
            finish() // Close activity when "Finish" is clicked
        }
    }


    private fun showScoreDialog(context: Context, score: Int, totalQuestions: Int, onFinish: () -> Unit) {
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(LayoutInflater.from(context))
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"

            if (percentage > 60) {
                scoreTitle.text = context.getString(R.string.success_alert)
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = context.getString(R.string.fail_alert)
                scoreTitle.setTextColor(Color.RED)
            }

            scoreSubtitle.text = "$score out of $totalQuestions are correct"

            finishBtn.setOnClickListener {
                onFinish() // Call the callback function
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialog.show()
    }


}