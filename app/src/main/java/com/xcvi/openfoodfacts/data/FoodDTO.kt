package com.xcvi.openfoodfacts.data

import com.google.gson.annotations.SerializedName
import com.xcvi.openfoodfacts.domain.FoodModel

data class SearchDTO(
    val count: Int = 0,
    val products: List<FoodDTO> = emptyList(),
)
data class BarcodeDTO(
    val product: FoodDTO? = null,
)


data class FoodDTO(
    val image_url: String? = null,
    val product_name: String = "",
    val id: String = "",
    val code: String = "",
    val nutriments: NutrimentsDTO? = null
)

data class NutrimentsDTO(
    @SerializedName("energy-kcal_100g")
    val calories: Double = 0.0,
    @SerializedName("carbohydrates_100g")
    val carbs: Double = 0.0,
    @SerializedName("fat_100g")
    val fats: Double = 0.0,
    @SerializedName("proteins_100g")
    val protein: Double = 0.0,
)










