package com.xcvi.openfoodfacts.api

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val count: Int,
    val products: List<Product>,
)
data class BarcodeResponse(
    val product: Product,
)


data class Product(
    val image_url: String? = null,
    val product_name: String,
    val id: String? = null,
    val code: String? = null,
    val nutriments: Nutriments? = null
)

data class Nutriments(
    @SerializedName("energy-kcal_100g")
    val calories: Int = 0,
    @SerializedName("carbohydrates_100g")
    val carbs: Double = 0.0,
    @SerializedName("fat_100g")
    val fats: Double = 0.0,
    @SerializedName("proteins_100g")
    val protein: Double = 0.0,
)