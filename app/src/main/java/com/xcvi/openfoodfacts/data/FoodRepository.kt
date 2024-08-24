package com.xcvi.openfoodfacts.data

import com.xcvi.openfoodfacts.domain.FoodModel

class FoodRepository(
    private val api: FoodApi
) {

    suspend fun catchBarcode(barcode: String): FoodModel? {
        return try {
            val response = api.getByBarcode(barcode)
            response.body()?.product?.toModel()
        } catch (e: Exception){
            null
        }
    }

    suspend fun searchByName(name: String, page:Int = 1): List<FoodModel>{
        return try {
            val response = api.getByName(name = name, page = page)
            val data = response.body()?.products?.map {
                it.toModel()
            }
            data ?: emptyList()
        } catch (e: Exception){
            emptyList()
        }
    }
}

