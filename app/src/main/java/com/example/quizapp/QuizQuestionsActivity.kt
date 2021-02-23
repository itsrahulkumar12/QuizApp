
package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity() , View.OnClickListener {

    private var mCurrentPosition : Int = 1
    private var mQuestionsList: ArrayList<Questions>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mCorrectAnswers : Int = 0
    private var mUserName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

    }

    private fun setQuestion(){

        val question =  mQuestionsList!![mCurrentPosition - 1 ]

        defaultOptionView()

        if (mCurrentPosition == mQuestionsList!!.size){
            btnSubmit.text = "FINISH"
        }else{
            btnSubmit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition" + "/" + progressBar.max

        tvQuestion.text = question!!.question
        ivFlag.setImageResource(question.image)
        tvOptionOne.text = question!!.optionOne
        tvOptionTwo.text = question!!.optionTwo 
        tvOptionThree.text = question!!.optionThree
        tvOptionFour.text = question!!.optionFour
    }

    private fun defaultOptionView(){
        val options = ArrayList<TextView>()
        options.add(0,tvOptionOne)
        options.add(1,tvOptionTwo)
        options.add(2,tvOptionThree)
        options.add(3,tvOptionFour)

        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.options_text_view_bg)

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvOptionOne ->{
                selectedOptionView(tvOptionOne, 1)
            }
            R.id.tvOptionTwo ->{
                selectedOptionView(tvOptionTwo , 2)
            }
            R.id.tvOptionThree -> {
                selectedOptionView(tvOptionThree, 3)
            }
            R.id.tvOptionFour ->{
                selectedOptionView(tvOptionFour, 4)
            }
            R.id.btnSubmit -> {
                 if (mSelectedOptionPosition == 0){
                     mCurrentPosition++

                     when{
                         mCurrentPosition <= mQuestionsList!!.size ->{
                             setQuestion()
                         }else -> {
                         val intent = Intent(this,ResultActivity::class.java)
                         intent.putExtra(Constants.USER_NAME , mUserName)
                         intent.putExtra(Constants.TOTAL_QUESTIONS ,  mQuestionsList!!.size)
                         intent.putExtra(Constants.CORRECT_ANSWERS , mCorrectAnswers )
                         startActivity(intent)
                         finish()
                         }
                     }
                 }else {
                     val question = mQuestionsList?.get(mCurrentPosition - 1)
                     if (question!!.correctAnswer != mSelectedOptionPosition){
                         answerView(mSelectedOptionPosition, R.drawable.incorrect_option_border)
                     }else{
                         mCorrectAnswers++
                     }
                     answerView(question.correctAnswer, R.drawable.correct_option_border)

                     if (mCurrentPosition == mQuestionsList!!.size){
                         btnSubmit.text = " FINISH"
                     }else{
                         btnSubmit.text = "GO TO NEXT QUESTION"
                     }
                     mSelectedOptionPosition = 0
                 }
            }
        }
    }

    private fun answerView(answer: Int , drawableView : Int){
        when(answer){
            1 -> {
                tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionView(tv: TextView , selectedOptionNum: Int){
        defaultOptionView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_options_text_view_bg)


    }
}