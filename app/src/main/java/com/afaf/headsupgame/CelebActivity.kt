package com.afaf.headsupgame


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CelebActivity : AppCompatActivity() {

    private var celebCount = 0
    lateinit var celebs: Celeb
    lateinit var landscapeLayout: LinearLayout
    lateinit var portraitLayout: ConstraintLayout
    lateinit var timerTextView: TextView
    lateinit var celebTextViews: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celeb)
        landscapeLayout = findViewById(R.id.celeb_info_layout)
        portraitLayout = findViewById(R.id.portrait_layout)
        timerTextView = findViewById(R.id.tvTimer)

        setCelebs()
        startTimer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateCelebViews()
            portraitLayout.visibility = INVISIBLE
            landscapeLayout.visibility = VISIBLE
        }
        else {
            ++celebCount
            landscapeLayout.visibility = INVISIBLE
            portraitLayout.visibility = VISIBLE
        }
    }

    private fun setCelebs() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface!!.getCelebs()?.enqueue(object: Callback<Celeb?> {

            override fun onFailure(call: Call<Celeb?>, t: Throwable) {
                Toast.makeText(this@CelebActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }

            override fun onResponse(call: Call<Celeb?>, response: Response<Celeb?>) {
                celebs = response.body()!!
            }
        })
    }

    private fun updateCelebViews() {
        celebTextViews = listOf (
            findViewById(R.id.tvCelebName),
            findViewById(R.id.tvCelebTaboo1),
            findViewById(R.id.tvCelebTaboo2),
            findViewById(R.id.tvCelebTaboo3)
        )
        val currentCeleb = celebs[celebCount]
        celebTextViews[0].text = currentCeleb.name
        celebTextViews[1].text = currentCeleb.taboo1
        celebTextViews[2].text = currentCeleb.taboo2
        celebTextViews[3].text = currentCeleb.taboo3
    }

    private fun startTimer() {
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                timerTextView.text = "Time: 0"
                timerTextView.setTextColor(Color.RED)
                startActivity(Intent(this@CelebActivity, MainActivity::class.java))
            }
        }.start()
    }
}