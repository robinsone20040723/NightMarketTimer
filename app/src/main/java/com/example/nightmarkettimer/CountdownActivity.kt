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
    private lateinit var priceText: TextView // 🆕 新增顯示金額的 TextView
    private lateinit var btnBack: Button

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInSeconds: Int = 60
    private var foodName: String = "小吃"
    private var hintMap: Map<Int, String> = emptyMap()
    private var totalPrice: Int = 0 // 🆕 接收金額用

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        timerText = findViewById(R.id.timerText)
        hintText = findViewById(R.id.hintText)
        priceText = findViewById(R.id.priceText) // 🆕 綁定金額欄位
        btnBack = findViewById(R.id.btnBack)

        foodName = intent.getStringExtra("foodName") ?: "小吃"
        timeLeftInSeconds = intent.getIntExtra("timeLeft", 60)
        totalPrice = intent.getIntExtra("totalPrice", 0) // 🆕 接收金額
        @Suppress("UNCHECKED_CAST")
        hintMap = intent.getSerializableExtra("hintMap") as? Map<Int, String> ?: emptyMap()

        // 顯示總金額
        priceText.text = "💰 總金額：${totalPrice} 元"

        btnBack.setOnClickListener {
            countDownTimer?.cancel()
            val intent = Intent(this, SelectStoreActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
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
                hintText.text = " 吃起來～"

                MediaPlayer.create(this@CountdownActivity, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI).start()
                showNotification("🍴 餐點完成提醒", "$foodName 已經準備好了，快去拿喔！")
            }
        }.start()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "food_ready_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "完成提醒通知",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, SelectStoreActivity::class.java)
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
