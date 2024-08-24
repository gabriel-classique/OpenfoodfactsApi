package com.xcvi.openfoodfacts.data

import com.xcvi.openfoodfacts.domain.FoodModel
import com.xcvi.openfoodfacts.domain.FoodResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepository(
    private val api: FoodApi
) {

    fun FoodDTO.toModel(): FoodModel {
        return FoodModel(
            id = this.id,
            name = this.product_name,
            calories = this.nutriments?.calories ?: 0.0,
            carbs = this.nutriments?.carbs ?: 0.0,
            fats = this.nutriments?.fats ?: 0.0,
            protein = this.nutriments?.protein ?: 0.0,
        )
    }


    suspend fun searchBarcode(barcode: String): FoodResponse<FoodModel?> {
        return try {
            val response = api.getByBarcode(barcode)
            val data = response.body()?.product?.toModel()
            if (data == null) {
                FoodResponse(
                    isSuccessful = false,
                    error = "",
                    result = null
                )
            } else {
                FoodResponse(
                    isSuccessful = true,
                    error = "",
                    result = data
                )
            }

        } catch (e: Exception) {
            println()
            FoodResponse(
                isSuccessful = false,
                error = "Barcode Exception: " + e.message,
                result = null
            )
        }
    }

    suspend fun searchByName(name: String, page: Int = 1): FoodResponse<List<FoodModel>> {
        return try {
            val response = api.getByName(name = name, page = page)
            val data = response.body()?.products?.map {
                it.toModel()
            }
            val isSuccessful = !data.isNullOrEmpty()
            if (isSuccessful) {
                FoodResponse(
                    isSuccessful = true,
                    error = "",
                    result = data ?: emptyList()
                )
            } else {
                FoodResponse(
                    isSuccessful = false,
                    error = "",
                    result = data ?: emptyList()
                )
            }

        } catch (e: Exception) {
            FoodResponse(
                isSuccessful = false,
                error = "Search Exception: " + e.message,
                result = emptyList()
            )
        }
    }
}

