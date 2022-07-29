package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition:Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    private var progressBar: ProgressBar? = null
    private var tv_progress: TextView? = null

    private var tv_question: TextView? = null
    private var iv_image: ImageView? = null

    private var tv_option_one: TextView? = null
    private var tv_option_two: TextView? = null
    private var tv_option_three: TextView? = null
    private var tv_option_four: TextView? = null

    private var btn_submit: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        tv_progress = findViewById<TextView>(R.id.tv_progress)

        tv_question = findViewById<TextView>(R.id.tv_question)
        iv_image = findViewById<ImageView>(R.id.iv_image)

        tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        tv_option_four = findViewById<TextView>(R.id.tv_option_four)

        btn_submit = findViewById<Button>(R.id.btn_submit)

        setQuestion()

        tv_option_one!!.setOnClickListener(this)
        tv_option_two!!.setOnClickListener(this)
        tv_option_three!!.setOnClickListener(this)
        tv_option_four!!.setOnClickListener(this)
        btn_submit!!.setOnClickListener(this)

    }

    private fun setQuestion(){
        val question = mQuestionsList!![mCurrentPosition-1]

        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit!!.text = "FINISH"
        }else{
            btn_submit!!.text = "SUBMIT"
        }

        progressBar!!.progress = mCurrentPosition
        tv_progress!!.text = "$mCurrentPosition" + "/" + progressBar!!.max

        tv_question!!.text = question!!.question
        iv_image!!.setImageResource(question.image)

        tv_option_one!!.text = question.optionOne
        tv_option_two!!.text = question.optionTwo
        tv_option_three!!.text = question.optionThree
        tv_option_four!!.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one!!)
        options.add(1, tv_option_two!!)
        options.add(2, tv_option_three!!)
        options.add(3, tv_option_four!!)

        for (option in options){
            option.setTextColor(resources.getColor(R.color.lightGrey))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one ->{
                selectedOptionView(tv_option_one!!, 1)
            }
            R.id.tv_option_two ->{
                selectedOptionView(tv_option_two!!, 2)
            }
            R.id.tv_option_three ->{
                selectedOptionView(tv_option_three!!, 3)
            }
            R.id.tv_option_four ->{
                selectedOptionView(tv_option_four!!, 4)
            }
            R.id.btn_submit -> {
                // if next question is clicked go to the next question
                //TODO make it so you have to click on something in order to move on to the next question
                //TODO make it so you can't change you answer after you submit
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        // if there are still questions left to ask then ask the next question
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }
                        // if not then show the end card
                        else ->{
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()

                        }
                    }
                }
                // if next question is not clicked that means it is on submit mode.
                // Check if the answer is right.
                else {
                    val question = mQuestionsList?.get(mCurrentPosition-1)

                    // if the selected textView is the wrong answer make it red
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }
                    // if it is the right answer increase the number of correct numbers gotten
                    else{
                        mCorrectAnswers++
                    }

                    // always show the correct answer
                    answerView(question!!.correctAnswer, R.drawable.correct_option_border_bg)

                    // if you reach the end of the quiz show finished as the next
                    if(mCurrentPosition == mQuestionsList!!.size){
                        btn_submit!!.text = "FINISH"
                    }
                    // else make the text next question
                    else{
                        btn_submit!!.text = "NEXT QUESTION"
                    }
                    // set selected position to zero to tell code to go to text question.
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 ->{
                tv_option_one!!.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 ->{
                tv_option_two!!.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 ->{
                tv_option_three!!.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 ->{
                tv_option_four!!.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }

        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber

        tv.setTextColor(resources.getColor(R.color.darkGrey))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )

    }
}