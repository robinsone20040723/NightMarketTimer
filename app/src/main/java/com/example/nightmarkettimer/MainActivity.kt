package com.example.nightmarkettimer  // ← 這要改成你的實際包名

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {

    // 改用資料模型
    private val foodList = listOf(
        FoodItem("🍗 雞排", 300, mapOf(180 to "雞排還有 3 分鐘喔～", 60 to "快聞到雞排的香味了～", 10 to "快好了，準備衝刺！")),
        FoodItem("🧄 臭豆腐", 180, mapOf(90 to "臭豆腐正飄香～", 30 to "準備好了嗎？", 10 to "快開動！")),
        FoodItem("🌭 大腸包小腸", 240, mapOf(120 to "還要 2 分鐘！", 60 to "肚子餓了嗎？", 10 to "快好了喔！")),
        FoodItem("🥟 煎餃", 150, mapOf(60 to "香酥酥的水餃快好了～", 10 to "準備沾醬！")),
        FoodItem("🍡 關東煮", 180, mapOf(90 to "熱呼呼的關東煮翻滾中～", 10 to "拿碗準備裝囉！")),
        FoodItem("🍢 黑輪", 120, mapOf(60 to "黑輪開始冒泡囉～", 10 to "可以開吃啦！")),
        FoodItem("🍹 木瓜牛奶", 90, mapOf(60 to "木瓜牛奶在打囉～", 10 to "新鮮現打完成！")),
        FoodItem("🍜 蚵仔麵線", 210, mapOf(120 to "蚵仔麵線香氣撲鼻～", 30 to "碗準備好囉～", 10 to "加點辣更好吃！")),
        FoodItem("🍩 雞蛋糕", 240, mapOf(120 to "雞蛋糕香氣出爐～", 10 to "準備拿紙袋裝！")),
        FoodItem("🍧 剉冰", 180, mapOf(90 to "冰正在剉喔～", 30 to "糖漿快淋好了～", 10 to "吃冰時間到！"))

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // ✅ 必加這行

        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinnerFoods)
        val btnStart = findViewById<Button>(R.id.btnStart)

        val foodNames = foodList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foodNames)
        spinner.adapter = adapter

        btnStart.setOnClickListener {
            val selectedName = spinner.selectedItem.toString()
            val selectedFood = foodList.find { it.name == selectedName } ?: return@setOnClickListener

            val intent = Intent(this, CountdownActivity::class.java)
            intent.putExtra("foodName", selectedFood.name)
            intent.putExtra("timeLeft", selectedFood.waitSeconds)
            intent.putExtra("hintMap", HashMap(selectedFood.hints)) // 要序列化 map
            startActivity(intent)
        }
    }
}

