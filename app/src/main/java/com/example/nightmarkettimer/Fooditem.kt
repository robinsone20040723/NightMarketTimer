package com.example.nightmarkettimer

data class FoodItem(
    val name: String,
    val waitSeconds: Int,
    val hints: Map<Int, String>  // 例如：180 -> "還有 3 分鐘喔～"
)
