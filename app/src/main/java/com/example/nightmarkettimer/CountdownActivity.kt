package com.example.nightmarkettimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.Locale

class CountdownActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var hintText: TextView
    private lateinit var btnBack: Button

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInSeconds: Int = 60
    private var foodName: String = "小吃"
    private var hintMap: Map<Int, String> = emptyMap()
    private var isCounting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        timerText = findViewById(R.id.timerText)
        hintText = findViewById(R.id.hintText)
        btnBack = findViewById(R.id.btnBack)

        foodName = intent.getStringExtra("foodName") ?: "小吃"
        timeLeftInSeconds = intent.getIntExtra("timeLeft", 60)
        hintMap = intent.getSerializableExtra("hintMap") as? Map<Int, String> ?: emptyMap()

        btnBack.setOnClickListener {
            countDownTimer?.cancel()
            finish()
        }

        startCountdown()
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(timeLeftInSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", secondsLeft / 60, secondsLeft % 60)
                timerText.text = "剩下：$timeFormatted"
                hintText.text = hintMap[secondsLeft] ?: hintText.text
            }

            override fun onFinish() {
                timerText.text = "✅ ${foodName} 好了，快去拿！"
                hintText.text = "🎉 吃起來～"

                // 播放提示音
                MediaPlayer.create(this@CountdownActivity, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI).start()

                // 顯示通知
                showNotification("🍴 小吃完成", "$foodName 已經好了，快去拿！")
            }
        }.start()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "food_done_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "小吃完成通知",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}
