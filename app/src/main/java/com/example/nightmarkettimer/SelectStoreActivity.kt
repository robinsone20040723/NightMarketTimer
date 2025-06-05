// SelectStoreActivity.kt
package com.example.nightmarkettimer

import android.content.Intent
import android.widget.TextView

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SelectStoreActivity : AppCompatActivity() {

    private lateinit var storeSpinner: Spinner
    private lateinit var btnNext: Button

    private val stores = listOf(
        Store("鹹酥雞店", listOf(
            FoodItem("🍗 鹹酥雞", 180, 60),
            FoodItem("🍠 地瓜球", 150, 40),
            FoodItem("🍖 雞軟骨", 200, 50),
            FoodItem("🍖 雞",10, 50)
        )),
        Store("麻辣燙店", listOf(
            FoodItem("🥬 高麗菜", 120, 30),
            FoodItem("🍜 冬粉", 100, 25),
            FoodItem("🥩 牛肉片", 180, 70)
        )),
        Store("雞排店", listOf(
            FoodItem("🔥 經典雞排", 200, 65),
            FoodItem("🧄 蒜味雞排", 220, 70),
            FoodItem("🧀 起司雞排", 240, 75)
        )),
        Store("串燒店", listOf(
            FoodItem("🍢 雞肉串", 160, 45),
            FoodItem("🍡 米血串", 130, 30),
            FoodItem("🌽 烤玉米", 240, 60)
        ))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_store)

        title = "請選擇店家 🍴"

        storeSpinner = findViewById(R.id.spinnerStores)
        btnNext = findViewById(R.id.btnNext)

        val storeNames = stores.map { it.name }
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            storeNames
        ) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                (view as? TextView)?.apply {
                    textSize = 24f  // ⬅ 字體變大
                    setTypeface(null, android.graphics.Typeface.BOLD)  // ⬅ 字體變粗
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as? TextView)?.apply {
                    textSize = 24f  // ⬅ 字體變大
                    setTypeface(null, android.graphics.Typeface.BOLD)  // ⬅ 字體變粗
                }
                return view
            }
        }
        storeSpinner.adapter = adapter






        btnNext.setOnClickListener {
            val selectedIndex = storeSpinner.selectedItemPosition
            val selectedStore = stores[selectedIndex]

            val intent = Intent(this, SelectItemsActivity::class.java)
            intent.putExtra("storeName", selectedStore.name)
            intent.putExtra("storeItems", ArrayList(selectedStore.items))
            startActivity(intent)
        }
    }
}