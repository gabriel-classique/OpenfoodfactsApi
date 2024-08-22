package com.xcvi.openfoodfacts.api

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
    val code: String? = null
)