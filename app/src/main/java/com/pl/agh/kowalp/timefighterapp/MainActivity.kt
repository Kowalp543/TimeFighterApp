package com.pl.agh.kowalp.timefighterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var gameScore: TextView
    private lateinit var gameTime: TextView
    private lateinit var gameButton: Button

    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60


    private val TAG = MainActivity::class.simpleName

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        gameScore = findViewById(R.id.game_score)
        gameTime = findViewById(R.id.game_time)
        gameButton = findViewById(R.id.game_button)

        gameButton.setOnClickListener { v ->

            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            bounceAnimation.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    incrementScore()
                }
            }
            )
            v.startAnimation(bounceAnimation)
        }

//        gameButton.setOnClickListener {
//            incrementScore()                       bez animacji
//       }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        Log.d(TAG, "onCreate called, score is: $score")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving score: $score, timeLeft: $timeLeft")
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun incrementScore() {
        if(!gameStarted) {
            startGame()
        }

        score +=1
        val newScore = getString(R.string.your_score, score)
        gameScore.text = newScore
    }

    private fun resetGame() {
        score = 0
        val initialScore = getString(R.string.your_score, score)
        gameScore.text = initialScore
        val initialTimeLeft = getString(R.string.time_left, 60)
        gameTime.text = initialTimeLeft

        gameStarted = false

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {  //Wyrazenie obiektowe
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() /1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                gameTime.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        gameScore.text = restoredScore

        val restoredTime = getString(R.string.time_left, timeLeft)
        gameTime.text = restoredTime

        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() /1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                gameTime.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_score, score),
            Toast.LENGTH_LONG).show()
    }
}