package com.example.nightmarkettimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class CountdownActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var hintText: TextView
    private lateinit var btnBack: Button  // 🔙 新增返回按鈕

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInSeconds: Int = 60
    private var foodName: String = "小吃"
    private var hintMap: Map<Int, String> = emptyMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        // 綁定畫面元件
        timerText = findViewById(R.id.timerText)
        hintText = findViewById(R.id.hintText)
        btnBack = findViewById(R.id.btnBack)  // 🔙 綁定返回按鈕

        // 接收來自主畫面的資料
        foodName = intent.getStringExtra("foodName") ?: "小吃"
        timeLeftInSeconds = intent.getIntExtra("timeLeft", 60)
        hintMap = intent.getSerializableExtra("hintMap") as? Map<Int, String> ?: emptyMap()

        // 🔙 設定返回按鈕功能
        btnBack.setOnClickListener {
            countDownTimer?.cancel()
            finish()  // 回到 MainActivity
        }

        startCountdown()
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(timeLeftInSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", secondsLeft / 60, secondsLeft % 60)
                timerText.text = "剩下：$timeFormatted"

                // 顯示提示訊息（如有）
                hintText.text = hintMap[secondsLeft] ?: hintText.text
            }

            override fun onFinish() {
                timerText.text = "✅ ${foodName} 好了，快去拿！"
                hintText.text = "🎉 吃起來～"

                // 播放提示音
                MediaPlayer.create(this@CountdownActivity, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI).start()
            }
        }.start()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}
