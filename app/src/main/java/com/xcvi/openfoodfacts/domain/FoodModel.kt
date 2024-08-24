package com.xcvi.openfoodfacts.domain

data class FoodModel(
    val id: String,
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fats: Double
)