package com.xcvi.openfoodfacts.data

import com.google.gson.annotations.SerializedName
import com.xcvi.openfoodfacts.domain.FoodModel

data class SearchResponse(
    val count: Int = 0,
    val products: List<FoodDto> = emptyList(),
)
data class BarcodeResponse(
    val product: FoodDto? = null,
)


data class FoodDto(
    val image_url: String? = null,
    val product_name: String = "",
    val id: String = "",
    val code: String = "",
    val nutriments: Nutriments? = null
)

data class Nutriments(
    @SerializedName("energy-kcal_100g")
    val calories: Double = 0.0,
    @SerializedName("carbohydrates_100g")
    val carbs: Double = 0.0,
    @SerializedName("fat_100g")
    val fats: Double = 0.0,
    @SerializedName("proteins_100g")
    val protein: Double = 0.0,
)


fun FoodDto.toModel(): FoodModel {
    return FoodModel(
        id = this.id,
        name = this.product_name,
        calories = this.nutriments?.calories ?: 0.0,
        carbs = this.nutriments?.carbs ?: 0.0,
        fats = this.nutriments?.fats ?: 0.0,
        protein = this.nutriments?.protein ?: 0.0,
    )
}






