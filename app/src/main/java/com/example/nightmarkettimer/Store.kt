package com.example.nightmarkettimer

import java.io.Serializable

data class Store(
    val name: String,
    val items: List<FoodItem>
) : Serializable
