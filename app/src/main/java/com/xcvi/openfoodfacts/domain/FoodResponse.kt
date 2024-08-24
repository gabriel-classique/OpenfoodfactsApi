package com.xcvi.openfoodfacts.domain

data class FoodResponse<T>(
    val isSuccessful: Boolean,
    val error: String,
    val result: T
)