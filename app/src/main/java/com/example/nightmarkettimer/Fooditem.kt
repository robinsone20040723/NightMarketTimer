package com.example.nightmarkettimer

import java.io.Serializable

data class FoodItem(
    val name: String,
    val waitSeconds: Int,
    val price: Int,
    val hints: Map<Int, String> = emptyMap()
) : Serializable
