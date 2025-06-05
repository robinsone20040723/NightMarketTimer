package com.example.nightmarkettimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable
import java.util.Locale

class SelectItemsActivity : AppCompatActivity() {

    private lateinit var layoutItems: LinearLayout
    private lateinit var btnSubmit: Button
    private lateinit var totalInfo: TextView

    private lateinit var storeName: String
    private lateinit var foodItems: List<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_items)

        layoutItems = findViewById(R.id.layoutItems)
        btnSubmit = findViewById(R.id.btnSubmit)
        totalInfo = findViewById(R.id.totalInfo)

        storeName = intent.getStringExtra("storeName") ?: "店家"
        @Suppress("UNCHECKED_CAST")
        foodItems = intent.getSerializableExtra("storeItems") as? ArrayList<FoodItem> ?: emptyList()

        title = "$storeName - 選擇品項"

        val checkBoxList = mutableListOf<CheckBox>()

        // ✅ 正確地動態產生 CheckBox 加進 layoutItems
        foodItems.forEach { item ->
            val checkBox = CheckBox(this)
            checkBox.text = "${item.name}（${item.price}元，${item.waitSeconds / 60}分${item.waitSeconds % 60}秒）"
            layoutItems.addView(checkBox)
            checkBox.textSize = 25f  // 設定文字大小為 18sp

            checkBoxList.add(checkBox)
        }

        // ✅ 點擊送出
        btnSubmit.setOnClickListener {
            val selectedItems = foodItems.zip(checkBoxList)
                .filter { it.second.isChecked }
                .map { it.first }

            if (selectedItems.isEmpty()) {
                totalInfo.text = "⚠️ 請至少選擇一樣品項！"
                return@setOnClickListener
            }

            val totalTime = selectedItems.maxOf { it.waitSeconds }
            val totalPrice = selectedItems.sumOf { it.price }

            val hintMap = selectedItems
                .flatMap { it.hints.entries.map { entry -> entry.toPair() } }
                .toMap()

            val nameSummary = selectedItems.joinToString("、") { it.name }

            // 顯示統計資訊
            totalInfo.text = "✅ 已選 ${selectedItems.size} 樣，預估最長等候時間：${formatTime(totalTime)}"

            val intent = Intent(this, CountdownActivity::class.java)
            intent.putExtra("foodName", nameSummary)
            intent.putExtra("timeLeft", totalTime)
            intent.putExtra("totalPrice", totalPrice)
            intent.putExtra("hintMap", hintMap as Serializable)
            startActivity(intent)
        }
    }

    private fun formatTime(seconds: Int): String {
        return String.format(Locale.getDefault(), "%02d分%02d秒", seconds / 60, seconds % 60)
    }
}
